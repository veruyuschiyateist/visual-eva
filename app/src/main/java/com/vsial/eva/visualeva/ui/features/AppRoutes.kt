package com.vsial.eva.visualeva.ui.features

import kotlinx.serialization.Serializable

@Serializable
data object PhotosRoute

@Serializable
data object CameraRoute

@Serializable
data class FiltersRoute(
    val imageUri: String
)