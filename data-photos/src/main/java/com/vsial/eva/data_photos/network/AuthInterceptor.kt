package com.vsial.eva.data_photos.network

import okhttp3.Interceptor
import okhttp3.Response

object AuthInterceptor {

    fun build(apiKey: String) = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID $apiKey")
                .build()
            return chain.proceed(newRequest)
        }
    }
}