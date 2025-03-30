package com.vsial.eva.visualeva.ui.features.camera

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vsial.eva.visualeva.CameraControllerEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun rememberCameraController(context: Context) = remember {
    EntryPointAccessors.fromApplication(
        context,
        CameraControllerEntryPoint::class.java
    ).cameraController()
}