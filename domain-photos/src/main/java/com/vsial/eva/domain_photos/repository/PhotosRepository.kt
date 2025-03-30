package com.vsial.eva.domain_photos.repository

import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.entities.CameraPhoto
import com.vsial.eva.domain_photos.entities.ImageFilterType

interface PhotosRepository {

    suspend fun loadLocalCameraPhotos(): Result<List<CameraPhoto>>

    suspend fun saveImageToCache(uri: String, filterType: ImageFilterType): Result<String>

    suspend fun saveImageToGallery(uri: String, filterType: ImageFilterType): Result<String>
}