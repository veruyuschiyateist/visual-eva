package com.vsial.eva.data_camera.repository

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import com.vsial.eva.domain_camera.entity.CameraType
import com.vsial.eva.domain_camera.entity.PhotoPath
import com.vsial.eva.domain_camera.repository.CameraRepository
import com.vsial.eva.domain_core.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cameraController: LifecycleCameraController
) : CameraRepository {

    private var isBackCamera = true

    override suspend fun getAllCameras(): Result<List<CameraType>> {
        return Result.of { TODO() }
    }

    override fun toggleCamera() {
        isBackCamera = !isBackCamera
        cameraController.cameraSelector = if (isBackCamera)
            CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
    }

    override fun selectCamera(cameraId: String) {
        TODO()
    }

    override suspend fun capturePhoto(): Result<PhotoPath> {
        TODO()
    }

}