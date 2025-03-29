package com.vsial.eva.data_photos.network

import com.vsial.eva.data_photos.model.UnsplashPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashService {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): List<UnsplashPhoto>

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String
    ): Response<UnsplashPhoto>
}