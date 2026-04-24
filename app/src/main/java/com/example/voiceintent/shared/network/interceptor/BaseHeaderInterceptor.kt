package com.example.voiceintent.shared.network.interceptor

import com.example.voiceintent.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class BaseHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val key = BuildConfig.GROQ_API_KEY

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $key")
            .build()
        return chain.proceed(request)
    }
}