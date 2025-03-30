package com.vsial.eva.data_photos.source

import com.vsial.eva.data_photos.network.UnsplashService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashPagingSource @Inject constructor(
    private val unsplashService: UnsplashService
) {
}