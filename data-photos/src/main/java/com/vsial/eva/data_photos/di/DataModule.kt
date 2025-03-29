package com.vsial.eva.data_photos.di

import com.vsial.eva.data_photos.network.AuthInterceptor
import com.vsial.eva.data_photos.network.UnsplashService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideUnsplashService(okHttpClient: OkHttpClient): UnsplashService {
        return Retrofit.Builder()
            .baseUrl(UNSPLASH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor.build(UNSPLASH_API_KEY))
            .build()
    }

    private const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"
    private const val UNSPLASH_API_KEY = "zBen9c7VCaqo7DVH1WYdyL8JLxP1UIjuAizzo1w3p9Q"
}