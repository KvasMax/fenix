package com.erros.minimax.fenix.di

import com.erros.minimax.fenix.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor() : Provider<OkHttpClient> {
    private val httpClient: OkHttpClient

    init {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
        }
        httpClient = httpClientBuilder.build()
    }

    override fun get() = httpClient
}