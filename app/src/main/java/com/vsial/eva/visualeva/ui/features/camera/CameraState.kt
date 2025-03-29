package com.vsial.eva.visualeva.ui.features.camera

import com.vsial.eva.domain_camera.entity.CameraType

data class CameraState(
    val availableCameras: List<CameraType> = emptyList(),
    val selectedCameraId: String? = null,
    val selectedFilter: String = "None"
)