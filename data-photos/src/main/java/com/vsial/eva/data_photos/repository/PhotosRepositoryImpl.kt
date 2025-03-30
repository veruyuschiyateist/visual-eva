package com.vsial.eva.data_photos.repository

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.entities.CameraPhoto
import com.vsial.eva.domain_photos.entities.ImageFilterType
import com.vsial.eva.domain_photos.repository.PhotosRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import androidx.core.graphics.createBitmap
import com.vsial.eva.data_photos.mappers.toColorMatrix
import java.io.FileOutputStream

@Singleton
class PhotosRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PhotosRepository {

    companion object {
        const val FILTERED_IMAGE_PREFIX = "filtered_image_"
    }

    override suspend fun loadLocalCameraPhotos(): Result<List<CameraPhoto>> {
        return Result.of {
            val contentResolver = context.contentResolver

            val images = mutableListOf<CameraPhoto>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
            )

            val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
            val selectionArgs = arrayOf("%DCIM/camera_photos%")
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
                ?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val imageUri = ContentUris.withAppendedId(uri, id)
                        images.add(CameraPhoto(id.toString(), imageUri.toString()))
                    }
                }

            images
        }
    }

    override suspend fun generateFilteredImageUri(
        uri: String,
        filterType: ImageFilterType
    ): Result<String> {
        return Result.of {
            val imageUri = uri.toUri()
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val sourceBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val resultBitmap = createBitmap(sourceBitmap.width, sourceBitmap.height)

            val canvas = Canvas(resultBitmap)
            val paint = Paint()

            filterType.toColorMatrix()?.let { matrix ->
                paint.colorFilter = ColorMatrixColorFilter(matrix)
            }

            canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

            val file =
                File(context.cacheDir, "$FILTERED_IMAGE_PREFIX${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { outputStream ->
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            cleanUpCache(exclude = file)

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            contentUri.toString()
        }
    }

    private fun cleanUpCache(exclude: File? = null) {
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { file ->
            if (file.name.startsWith(FILTERED_IMAGE_PREFIX) && file != exclude) {
                file.delete()
            }
        }
    }
}