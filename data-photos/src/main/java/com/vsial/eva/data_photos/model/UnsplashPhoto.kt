package com.vsial.eva.data_photos.model

/*
    Unsplash API guarantees these fields will always be present in the response.
 */
data class UnsplashPhoto(
    val id: String,
    val urls: Urls
) {

    data class Urls(
        val regular: String
    )
}

