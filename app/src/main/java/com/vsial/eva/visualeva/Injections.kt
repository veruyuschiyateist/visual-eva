package com.vsial.eva.visualeva

import androidx.camera.view.LifecycleCameraController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CameraControllerEntryPoint {

    fun cameraController(): LifecycleCameraController
}