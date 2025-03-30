package com.vsial.eva.visualeva.ui.models

import com.vsial.eva.domain_photos.entities.ImageFilterType
import com.vsial.eva.visualeva.ui.mappers.UiImageFilterType

fun ImageFilterType.toUi(): UiImageFilterType {
    return when (this) {
        ImageFilterType.NONE -> UiImageFilterType.NONE
        ImageFilterType.BLACK_WHITE -> UiImageFilterType.BLACK_WHITE
        ImageFilterType.WARM -> UiImageFilterType.WARM
        ImageFilterType.FILM -> UiImageFilterType.FILM
        ImageFilterType.CINEMATIC -> UiImageFilterType.CINEMATIC
        ImageFilterType.SEPIA -> UiImageFilterType.SEPIA
        ImageFilterType.COOL -> UiImageFilterType.COOL
        ImageFilterType.FADE -> UiImageFilterType.FADE
        ImageFilterType.VIBRANT -> UiImageFilterType.VIBRANT
    }
}

fun UiImageFilterType.toDomain(): ImageFilterType {
    return when (this) {
        UiImageFilterType.NONE -> ImageFilterType.NONE
        UiImageFilterType.BLACK_WHITE -> ImageFilterType.BLACK_WHITE
        UiImageFilterType.WARM -> ImageFilterType.WARM
        UiImageFilterType.FILM -> ImageFilterType.FILM
        UiImageFilterType.CINEMATIC -> ImageFilterType.CINEMATIC
        UiImageFilterType.SEPIA -> ImageFilterType.SEPIA
        UiImageFilterType.COOL -> ImageFilterType.COOL
        UiImageFilterType.FADE -> ImageFilterType.FADE
        UiImageFilterType.VIBRANT -> ImageFilterType.VIBRANT
    }
}