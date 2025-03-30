package com.vsial.eva.visualeva.ui.features.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsial.eva.domain_camera.interactors.CapturePhotoUseCase
import com.vsial.eva.domain_camera.interactors.GetAvailableCamerasUseCase
import com.vsial.eva.domain_camera.interactors.SwitchCameraUseCase
import com.vsial.eva.domain_core.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val switchCameraUseCase: SwitchCameraUseCase,
    private val capturePhotoUseCase: CapturePhotoUseCase,
    private val getAvailableCamerasUseCase: GetAvailableCamerasUseCase
) : ViewModel() {

    private val cameraMutableState = MutableStateFlow(CameraState())
    val cameraFlow = cameraMutableState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = getAvailableCamerasUseCase.invoke()) {
                is Result.Success -> {
                    val result = result.data
                    cameraMutableState.value = cameraMutableState.value.copy(
                        availableCameras = result,
                        selectedCameraId = result.firstOrNull()?.id
                    )
                }

                is Result.Error -> {
                }
            }
        }
    }

    fun selectCamera(id: String) {
        viewModelScope.launch {
            val cameraInfo = cameraMutableState.value.availableCameras.firstOrNull() { it.id == id }
                ?: return@launch

            switchCameraUseCase.selectCamera(id)
            cameraMutableState.value =
                cameraMutableState.value.copy(selectedCameraId = cameraInfo.id)
        }
    }

    fun toggleCamera() {
        switchCameraUseCase.toggleFrontBack()
    }

    fun capturePhoto() {
        viewModelScope.launch {
            cameraMutableState.value = cameraMutableState.value.copy(isLoading = true)
            val result = capturePhotoUseCase.invoke()

            when (result) {
                is Result.Error -> { /* TODO() */
                }

                is Result.Success<*> -> {
                    cameraMutableState.value = cameraMutableState.value.copy(isLoading = false)
                }
            }

        }
    }
}