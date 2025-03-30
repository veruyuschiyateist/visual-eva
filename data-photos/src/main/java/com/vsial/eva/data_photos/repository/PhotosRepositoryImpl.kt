package com.vsial.eva.data_photos.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.model.CameraPhoto
import com.vsial.eva.domain_photos.repository.PhotosRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PhotosRepository {

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
}