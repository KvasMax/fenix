package com.erros.minimax.fenix.view.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.erros.minimax.fenix.view.utils.logDebug

/**
 * Created by minimax on 5/1/18.
 */
abstract class BaseActivity<Self : BaseView<Self, P>, P : BasePresenter<P, Self, *>> : AppCompatActivity(), BaseView<Self, P> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logDebug("onCreate")
        setContentView(layoutId)
        onViewCreated()
        presenter.onAttachTo(this as Self)
        presenter.onInit(savedInstanceState, initialMsg)
    }

    abstract fun onViewCreated()

    abstract val initialMsg: Msg

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveState(outState)
    }

    override fun onStart() {
        super.onStart()
        logDebug("onStart")
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        logDebug("onStop")
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        logDebug("onDestroy")
        presenter.onDetach()
    }


}