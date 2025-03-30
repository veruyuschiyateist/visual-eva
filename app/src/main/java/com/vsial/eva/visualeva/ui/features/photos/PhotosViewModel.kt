package com.vsial.eva.visualeva.ui.features.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.interactors.GetAllLocalPhotosUseCase
import com.vsial.eva.domain_photos.entities.CameraPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getAllLocalPhotosUseCase: GetAllLocalPhotosUseCase
) : ViewModel() {

    private val localPhotosMutableState =
        MutableStateFlow<Result<List<CameraPhoto>>>(Result.Success(emptyList()))
    val localPhotosFlow = localPhotosMutableState.asStateFlow()

    init {
        viewModelScope.launch {
            localPhotosMutableState.value = getAllLocalPhotosUseCase.invoke()
        }
    }
}