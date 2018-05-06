package com.erros.minimax.fenix.di

import android.content.Context
import com.erros.minimax.fenix.data.PlaceholderApi
import com.erros.minimax.fenix.data.PlaceholderRepository
import okhttp3.OkHttpClient
import toothpick.config.Module

/**
 * Created by minimax on 1/28/18.
 */
class AppModule(context: Context) : Module() {

    init {
        bind(Context::class.java).toInstance(context.applicationContext)
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(String::class.java).withName(PlaceholderPath::class.java).toProvider(PlaceholderPathProvider::class.java).providesSingletonInScope()
        bind(PlaceholderApi::class.java).toProvider(PlaceholderApiProvider::class.java).providesSingletonInScope()
        bind(PlaceholderRepository::class.java).singletonInScope()
    }


}