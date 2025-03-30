package com.vsial.eva.domain_photos.repository

import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.model.CameraPhoto

interface PhotosRepository {

    suspend fun loadLocalCameraPhotos(): Result<List<CameraPhoto>>

}