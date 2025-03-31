package com.vsial.eva.visualeva.ui.features.photos

import com.vsial.eva.domain_photos.entities.CameraPhoto

sealed interface PhotosUiState {

    object Loading : PhotosUiState

    object Empty : PhotosUiState

    data class Success(val data: List<CameraPhoto>) : PhotosUiState

    data class Error(val error: Throwable, val message: String) : PhotosUiState
}