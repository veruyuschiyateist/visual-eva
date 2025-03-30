package com.vsial.eva.domain_photos.interactors

import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_core.interactor.UseCaseIoDispatcherWithRequest
import com.vsial.eva.domain_photos.entities.ShareImageRequest
import com.vsial.eva.domain_photos.repository.PhotosRepository
import javax.inject.Inject

class ShareFilteredImageUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) : UseCaseIoDispatcherWithRequest<Result<String>, ShareImageRequest>() {

    override suspend fun invoke(): Result<String> {
        return photosRepository.generateFilteredImageUri(
            uri = request.imageUri,
            filterType = request.filter
        )
    }
}