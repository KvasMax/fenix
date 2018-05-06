package com.erros.minimax.fenix.di

import com.erros.minimax.fenix.data.PlaceholderApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by milkman on 31.01.18.
 */
class PlaceholderApiProvider
@Inject constructor(private val okHttpClient: OkHttpClient,
                    @PlaceholderPath private val serverPath: String)
    : Provider<PlaceholderApi> {

    override fun get(): PlaceholderApi =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(serverPath)
                    .build()
                    .create(PlaceholderApi::class.java)

}