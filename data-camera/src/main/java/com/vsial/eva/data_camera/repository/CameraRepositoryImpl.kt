package com.vsial.eva.data_camera.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.vsial.eva.data_camera.processors.BitmapTransformer
import com.vsial.eva.domain_camera.entity.CameraInfo
import com.vsial.eva.domain_camera.entity.CameraType
import com.vsial.eva.domain_camera.entity.PhotoPath
import com.vsial.eva.domain_camera.repository.CameraRepository
import com.vsial.eva.domain_core.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class CameraRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cameraController: LifecycleCameraController
) : CameraRepository {

    private var isBackCamera = true

    @OptIn(ExperimentalCamera2Interop::class)
    override suspend fun getAllCameras(): Result<List<CameraInfo>> {
        return Result.of {
            val cameraProvider = ProcessCameraProvider.getInstance(context).get()

            cameraProvider.availableCameraInfos.mapNotNull { camInfo ->
                val camera2Info = Camera2CameraInfo.from(camInfo)
                val cameraId = camera2Info.cameraId
                val characteristics = camera2Info.getCameraCharacteristic(
                    CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS
                )

                val minFocusDistance = camera2Info.getCameraCharacteristic(
                    CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE
                )

                val lensFacing = camera2Info.getCameraCharacteristic(
                    CameraCharacteristics.LENS_FACING
                )

                val focalLength = characteristics?.firstOrNull() ?: return@mapNotNull null

                val type = when {
                    lensFacing == CameraCharacteristics.LENS_FACING_FRONT -> CameraType.FRONT

                    minFocusDistance != null && minFocusDistance < 0.1f -> CameraType.MACRO

                    focalLength < 2.0f -> CameraType.ULTRA_WIDE

                    focalLength > 3.5f -> CameraType.TELEPHOTO

                    else -> CameraType.WIDE
                }

                CameraInfo(id = cameraId, label = type)
            }
        }
    }

    override fun toggleCamera() {
        isBackCamera = !isBackCamera
        cameraController.cameraSelector = if (isBackCamera)
            CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
    }

    @OptIn(ExperimentalCamera2Interop::class)
    override fun selectCamera(cameraId: String) {
        val cameraSelector = CameraSelector.Builder()
            .addCameraFilter { cameraInfos ->
                cameraInfos.filter { cameraInfo ->
                    val camera2Info = Camera2CameraInfo.from(cameraInfo)
                    camera2Info.cameraId == cameraId
                }
            }
            .build()

        cameraController.cameraSelector = cameraSelector
    }

    override suspend fun capturePhoto(): Result<PhotoPath> {
        return suspendCancellableCoroutine<Result<PhotoPath>> { continuation ->
            cameraController.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val isFront = !isBackCamera

                        val transformedBitmap =
                            BitmapTransformer.transform(image, isFront)

                        val result = saveImage(transformedBitmap)
                        image.close()

                        continuation.resume(result)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)

                        continuation.resume(Result.Error(exception))
                    }
                }
            )
        }
    }

    private fun saveImage(capturePhotoBitmap: Bitmap): Result<PhotoPath> {
        val resolver: ContentResolver = context.applicationContext.contentResolver
    
        val imageCollection: Uri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )

            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val nowTimestamp: Long = System.currentTimeMillis()
        val imageContentValues: ContentValues = ContentValues().apply {

            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}" + ".jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimestamp)
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/camera_photos"
                )
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.Images.Media.DATE_TAKEN, nowTimestamp)
                put(MediaStore.Images.Media.DATE_ADDED, nowTimestamp)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowTimestamp)
                put(MediaStore.Images.Media.AUTHOR, "visual eva")
                put(MediaStore.Images.Media.DESCRIPTION, "visual eva")
            }
        }

        val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

        val result: Result<PhotoPath> = imageMediaStoreUri?.let { uri ->
            runCatching {
                resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                    checkNotNull(outputStream) { "Couldn't create file for gallery, MediaStore output stream is null" }
                    capturePhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)
                }

                Result.Success(PhotoPath(uri.toString()))
            }.getOrElse { exception: Throwable ->
                exception.message?.let(::println)
                resolver.delete(uri, null, null)

                Result.Error(exception)
            }
        } ?: run {
            Result.Error(Exception("Couldn't create file for gallery"))
        }

        return result
    }

}