package com.vsial.eva.domain_camera.interactors

import com.vsial.eva.domain_camera.entity.PhotoPath
import com.vsial.eva.domain_camera.repository.CameraRepository
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_core.interactor.DefaultIoDispatcherUseCase
import javax.inject.Inject

class CapturePhotoUseCase @Inject constructor(
    private val cameraRepository: CameraRepository
) : DefaultIoDispatcherUseCase<Result<PhotoPath>>() {

    override suspend fun invoke(): Result<PhotoPath> {
        return cameraRepository.capturePhoto()
    }
}