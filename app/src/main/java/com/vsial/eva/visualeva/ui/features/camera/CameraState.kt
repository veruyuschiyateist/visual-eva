package com.vsial.eva.visualeva.ui.features.camera

import android.graphics.Bitmap
import com.vsial.eva.domain_camera.entity.CameraInfo

data class CameraState(
    val isLoading: Boolean = false,
    val availableCameras: List<CameraInfo> = emptyList(),
    val selectedCameraId: String? = null,
    val selectedFilter: String = "None",
    val capturedImage: Bitmap? = null
)