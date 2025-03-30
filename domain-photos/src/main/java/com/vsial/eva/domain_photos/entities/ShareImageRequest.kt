package com.vsial.eva.domain_photos.entities

data class ShareImageRequest(
    val imageUri: String,
    val filter: ImageFilterType
)