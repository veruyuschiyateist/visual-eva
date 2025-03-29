package com.vsial.eva.domain_camera.interactors

import com.vsial.eva.domain_camera.entity.CameraType
import com.vsial.eva.domain_camera.repository.CameraRepository
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_core.interactor.DefaultIoDispatcherUseCase
import javax.inject.Inject

class GetAvailableCamerasUseCase @Inject constructor(
    private val cameraRepository: CameraRepository
) : DefaultIoDispatcherUseCase<Result<List<CameraType>>>() {

    override suspend fun invoke(): Result<List<CameraType>> {
        return cameraRepository.getAllCameras()
    }
}