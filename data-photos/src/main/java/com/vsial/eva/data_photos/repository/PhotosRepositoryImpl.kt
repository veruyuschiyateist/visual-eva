package com.vsial.eva.data_photos.repository

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
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
import com.vsial.eva.data_photos.processors.ImageProcessingService
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

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

    override suspend fun saveImageToCache(
        uri: String,
        filterType: ImageFilterType
    ): Result<String> {
        return Result.of {
            val bitmap = ImageProcessingService.generateFilteredBitmap(context, uri, filterType)

            val file =
                File(context.cacheDir, "$FILTERED_IMAGE_PREFIX${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }

            cleanUpCache(file)

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            ).toString()
        }
    }

    override suspend fun saveImageToGallery(
        uri: String,
        filterType: ImageFilterType
    ): Result<String> {
        return Result.of {
            val bitmap = ImageProcessingService.generateFilteredBitmap(context, uri, filterType)
            val resolver = context.contentResolver

            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val fileName = "FILTERED_IMG_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "${Environment.DIRECTORY_DCIM}/camera_photos"
                    )
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val savedUri = resolver.insert(imageCollection, contentValues)
                ?: throw IOException("Failed to create MediaStore entry")

            resolver.openOutputStream(savedUri)?.use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            } ?: throw IOException("Failed to open output stream for $savedUri")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(savedUri, contentValues, null, null)
            }

            savedUri.toString()
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