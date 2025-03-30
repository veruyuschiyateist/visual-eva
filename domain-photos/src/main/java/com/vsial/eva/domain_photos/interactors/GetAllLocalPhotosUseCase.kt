package com.vsial.eva.domain_photos.interactors

import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_core.interactor.DefaultIoDispatcherUseCase
import com.vsial.eva.domain_photos.model.CameraPhoto
import com.vsial.eva.domain_photos.repository.PhotosRepository
import javax.inject.Inject

class GetAllLocalPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) : DefaultIoDispatcherUseCase<Result<List<CameraPhoto>>>() {

    override suspend fun invoke(): Result<List<CameraPhoto>> {
        return photosRepository.loadLocalCameraPhotos()
    }
}