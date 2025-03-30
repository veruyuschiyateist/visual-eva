package com.vsial.eva.domain_photos.repository

import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.entities.CameraPhoto
import com.vsial.eva.domain_photos.entities.ImageFilterType

interface PhotosRepository {

    suspend fun loadLocalCameraPhotos(): Result<List<CameraPhoto>>

    suspend fun generateFilteredImageUri(uri: String, filterType: ImageFilterType): Result<String>
}