package com.vsial.eva.domain_camera.interactors

import com.vsial.eva.domain_camera.entity.CameraInfo
import com.vsial.eva.domain_camera.repository.CameraRepository
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_core.interactor.DefaultIoDispatcherUseCase
import javax.inject.Inject

class GetAvailableCamerasUseCase @Inject constructor(
    private val cameraRepository: CameraRepository
) : DefaultIoDispatcherUseCase<Result<List<CameraInfo>>>() {

    override suspend fun invoke(): Result<List<CameraInfo>> {
        return cameraRepository.getAllCameras()
    }
}