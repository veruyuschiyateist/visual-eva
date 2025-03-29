package com.vsial.eva.visualeva.ui.features.camera

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CameraControllerEntryPoint {

    fun cameraController(): LifecycleCameraController
}

@Composable
fun rememberCameraController(context: Context) = remember {
    EntryPointAccessors.fromApplication(
        context,
        CameraControllerEntryPoint::class.java
    ).cameraController()
}