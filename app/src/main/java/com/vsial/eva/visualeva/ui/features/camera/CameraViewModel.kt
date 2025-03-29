package com.vsial.eva.visualeva.ui.features.camera

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsial.eva.domain_camera.interactors.CapturePhotoUseCase
import com.vsial.eva.domain_camera.interactors.GetAvailableCamerasUseCase
import com.vsial.eva.domain_camera.interactors.SwitchCameraUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val switchCameraUseCase: SwitchCameraUseCase,
    private val capturePhotoUseCase: CapturePhotoUseCase,
    private val getAvailableCamerasUseCase: GetAvailableCamerasUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            Log.d("CameraViewModel", "${getAvailableCamerasUseCase.invoke()}")
        }
    }

    fun toggleCamera() {
        switchCameraUseCase.toggleFrontBack()
    }

    fun capturePhoto() {
        viewModelScope.launch {
            capturePhotoUseCase.invoke()
        }
    }
}