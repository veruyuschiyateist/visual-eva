package com.vsial.eva.data_camera.repository

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.vsial.eva.domain_camera.entity.CameraInfo
import com.vsial.eva.domain_camera.entity.CameraType
import com.vsial.eva.domain_camera.entity.PhotoPath
import com.vsial.eva.domain_camera.repository.CameraRepository
import com.vsial.eva.domain_core.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
        return Result.of {
            val outputDir = File(context.cacheDir, "camera_photos").apply { mkdirs() }
            val photoFile =
                File.createTempFile("IMG_${System.currentTimeMillis()}", ".jpg", outputDir)

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            suspendCancellableCoroutine<PhotoPath> { continuation ->
                cameraController.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            continuation.resume(PhotoPath(photoFile.absolutePath))
                        }

                        override fun onError(exception: ImageCaptureException) {
                            continuation.resumeWithException(exception)
                        }
                    }
                )
            }
        }
    }
}