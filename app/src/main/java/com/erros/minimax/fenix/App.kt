package com.erros.minimax.fenix

import android.app.Application
import com.erros.minimax.fenix.di.DIManager
import com.erros.minimax.fenix.view.utils.logDebug

/**
 * Created by minimax on 5/1/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        logDebug("onCreate")
        DIManager.initModules(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logDebug("onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logDebug("onTrimMemory: $level")
    }

    override fun onTerminate() {
        super.onTerminate()
        logDebug("onTerminate")
    }

}