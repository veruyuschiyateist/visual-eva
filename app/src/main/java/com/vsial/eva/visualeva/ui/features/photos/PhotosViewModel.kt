package com.vsial.eva.visualeva.ui.features.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.entities.CameraPhoto
import com.vsial.eva.domain_photos.interactors.ObserveLocalPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val observeLocalPhotosUseCase: ObserveLocalPhotosUseCase
) : ViewModel() {

    private val photosMutableState = MutableStateFlow<PhotosUiState>(PhotosUiState.Empty)
    val photosState = photosMutableState.asStateFlow()
        
    init {
        viewModelScope.launch {
            photosMutableState.emit(value = PhotosUiState.Loading)

            observeLocalPhotosUseCase()
                .collect { result ->
                    val state = when (result) {
                        is Result.Success<List<CameraPhoto>> -> {
                            val photos = result.data
                            if (photos.isEmpty()) PhotosUiState.Empty
                            else PhotosUiState.Success(photos)
                        }

                        is Result.Error -> {
                            PhotosUiState.Error(
                                error = result.error,
                                message = result.error.localizedMessage ?: "Failed to fetch photos."
                            )
                        }
                    }

                    photosMutableState.emit(state)
                }
        }
    }
}