package com.erros.minimax.fenix.view.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.erros.minimax.fenix.logDebug

/**
 * Created by minimax on 5/1/18.
 */
abstract class BaseActivity<Self : BaseView<Self, P>, P : BasePresenter<P, Self, *>> : AppCompatActivity(), BaseView<Self, P> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logDebug("onCreate")
        setContentView(layoutId)
        onViewCreated()
        getBasePresenter().onAttachTo(this as Self)
        getBasePresenter().onInit(savedInstanceState, firstMsg)
    }

    abstract val firstMsg: Msg

    override fun onDestroy() {
        super.onDestroy()
        logDebug("onDestroy")
        getBasePresenter().onDetach()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getBasePresenter().onSaveState(outState)
    }

    abstract fun onViewCreated()
}