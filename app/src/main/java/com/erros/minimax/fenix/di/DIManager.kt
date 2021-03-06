package com.erros.minimax.fenix.di

import android.app.Application
import com.erros.minimax.fenix.BuildConfig
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.PlaceholderApi
import com.erros.minimax.fenix.data.PlaceholderRepository
import com.erros.minimax.fenix.data.ResourceProvider
import com.erros.minimax.fenix.view.detail.DetailPresenter
import com.erros.minimax.fenix.view.main.MainPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Scopes {
    const val MAIN_SCOPE = "MAIN_SCOPE"
    const val DETAIL_SCOPE = "DETAIL_SCOPE"
}

object DIManager {

    public const val baseUrlName = "baseUrlName"

    fun initModules(application: Application) {
        application.startKoin(application, listOf(appModule))
    }

    private val appModule = module {

        single {
            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
            }
            httpClientBuilder.build()
        }

        single(baseUrlName) {
            androidContext().getString(R.string.placeholder_url)
        }

        single {
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(get())
                    .baseUrl(get<String>(name = baseUrlName))
                    .build()
                    .create(PlaceholderApi::class.java)
        }

        single {
            ResourceProvider(androidContext())
        }

        single {
            PlaceholderRepository(get())
        }

        scope(Scopes.MAIN_SCOPE) { MainPresenter(get(), get()) }

        scope(Scopes.DETAIL_SCOPE) { DetailPresenter(get(), get()) }

    }
}