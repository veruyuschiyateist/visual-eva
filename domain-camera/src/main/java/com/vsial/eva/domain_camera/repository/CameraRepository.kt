package com.vsial.eva.domain_camera.repository

import com.vsial.eva.domain_camera.entity.CameraType
import com.vsial.eva.domain_camera.entity.PhotoPath
import com.vsial.eva.domain_core.Result

interface CameraRepository {

    suspend fun getAllCameras(): Result<List<CameraType>>

    fun toggleCamera()

    fun selectCamera(id: String)

    suspend fun capturePhoto(): Result<PhotoPath>
}