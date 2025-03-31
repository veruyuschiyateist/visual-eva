package com.vsial.eva.domain_photos.interactors

import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_core.interactor.DefaultIoDispatcherUseCase
import com.vsial.eva.domain_photos.entities.CameraPhoto
import com.vsial.eva.domain_photos.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocalPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) : DefaultIoDispatcherUseCase<Flow<Result<List<CameraPhoto>>>>() {

    override suspend fun invoke(): Flow<Result<List<CameraPhoto>>> {
        return photosRepository.observeLocalPhotos()
    }
}