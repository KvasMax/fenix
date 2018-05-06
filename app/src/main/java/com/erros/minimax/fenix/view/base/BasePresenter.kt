package com.erros.minimax.fenix.view.base

import android.os.Bundle
import android.os.Parcelable

/**
 * Created by minimax on 5/1/18.
 */
abstract class BasePresenter<Self : BasePresenter<Self, V, S>, V : BaseView<V, Self>, S : State> : IBasePresenter<Self, V> {

    protected var view: V? = null

    override fun onAttachTo(view: V) {
        this.view = view
    }

    override fun onInit(savedInstanceState: Bundle?, msg: Msg) {
        savedInstanceState?.getParcelable<Parcelable>(getBundleKey())?.takeIf { it is State }?.let {
            actionScheduler.state = it as S
            actionScheduler.render()
        }
        if (actionScheduler.state == null) {
            actionScheduler.state = initialState()
            actionScheduler.accept(msg)
        }
    }

    abstract val actionScheduler: ActionScheduler<S>
    abstract fun initialState(): S

    private fun getBundleKey() = this.javaClass.canonicalName

    override fun onDetach() {
        view = null
    }

    override fun onSaveState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(getBundleKey(), actionScheduler.state)
    }
}