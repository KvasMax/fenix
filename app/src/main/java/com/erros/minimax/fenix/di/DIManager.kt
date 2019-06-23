package com.erros.minimax.fenix.di

import android.app.Application
import com.erros.minimax.fenix.BuildConfig
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.*
import com.erros.minimax.fenix.view.detail.DetailPresenter
import com.erros.minimax.fenix.view.main.MainPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DIManager {

    public const val baseUrlName = "baseUrlName"

    fun initModules(application: Application) {
        startKoin {
            androidContext(application)
            modules(listOf(appModule))
        }
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

        single(named(baseUrlName)) {
            androidContext().getString(R.string.placeholder_url)
        }

        single {
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(get())
                    .baseUrl(get<String>(named(baseUrlName)))
                    .build()
                    .create(PlaceholderApi::class.java)
        }

        single {
            ResourceProvider(androidContext())
        }

        single {
            PlaceholderRepository(get())
        }

        single {
            SQLiteStateStorage(androidContext()) as StateStorage
        }

        factory {
            MainPresenter(get(), get(), get())
        }

        factory {
            DetailPresenter(get(), get(), get())
        }

    }
}