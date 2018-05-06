package com.erros.minimax.fenix

import android.app.Application
import com.erros.minimax.fenix.di.AppModule
import com.erros.minimax.fenix.di.Scopes
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

/**
 * Created by minimax on 5/1/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        logDebug("onCreate")
        initToothpick()
        openAppScope()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logDebug("onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logDebug("onTrimMemory")
    }

    override fun onTerminate() {
        super.onTerminate()
        logDebug("onTerminate")
    }

    private fun initToothpick() {
        val configuration = if (BuildConfig.DEBUG) Configuration.forDevelopment() else Configuration.forProduction()

        Toothpick.setConfiguration(configuration.disableReflection()
                .preventMultipleRootScopes())

        FactoryRegistryLocator.setRootRegistry(com.erros.minimax.fenix.FactoryRegistry())
        MemberInjectorRegistryLocator.setRootRegistry(com.erros.minimax.fenix.MemberInjectorRegistry())
    }

    private fun openAppScope() {
        val appScope = Toothpick.openScope(Scopes.APP)
        appScope.installModules(AppModule(this))
    }
}