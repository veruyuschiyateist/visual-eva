package com.vsial.eva.visualeva.ui.features.camera

data class CameraState(
    val availableCameras: List<CameraType> = emptyList(),
    val selectedCameraId: String? = null,
    val selectedFilter: String = "None"
)