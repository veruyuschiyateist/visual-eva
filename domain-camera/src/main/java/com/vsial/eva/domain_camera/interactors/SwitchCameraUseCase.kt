package com.vsial.eva.domain_camera.interactors

import com.vsial.eva.domain_camera.repository.CameraRepository
import javax.inject.Inject

class SwitchCameraUseCase @Inject constructor(
    private val cameraRepository: CameraRepository
) {
    fun toggleFrontBack() = cameraRepository.toggleCamera()

    fun selectCamera(cameraId: String) = cameraRepository.selectCamera(cameraId)
}