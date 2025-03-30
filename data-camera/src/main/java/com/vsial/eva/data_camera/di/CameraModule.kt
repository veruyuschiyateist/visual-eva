package com.vsial.eva.data_camera.di

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import com.vsial.eva.data_camera.repository.CameraRepositoryImpl
import com.vsial.eva.domain_camera.repository.CameraRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {

    @Provides
    @Singleton
    fun provideCameraController(
        @ApplicationContext context: Context
    ): LifecycleCameraController {
        return LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS
            )
        }
    }
    
    @Provides
    @Singleton
    fun bindCameraRepository(
        cameraRepository: CameraRepositoryImpl
    ): CameraRepository = cameraRepository
}