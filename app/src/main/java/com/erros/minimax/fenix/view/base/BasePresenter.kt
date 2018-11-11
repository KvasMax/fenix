package com.erros.minimax.fenix.view.base

import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Created by minimax on 5/1/18.
 */
abstract class BasePresenter<Self : BasePresenter<Self, V, S>, V : BaseView<V, Self>, S : State> : IBasePresenter<Self, V> {

    private var viewWeakRef: WeakReference<V>? = null
    private val bundleKey = this.javaClass.canonicalName

    protected val view: V?
        get() = viewWeakRef?.get()

    protected abstract val initialState: S
    protected abstract val actionExecutor: ActionExecutor<S>

    protected fun updateView(operations: V.() -> Unit): Boolean {
        view?.let {
            operations.invoke(it)
            return true
        }
        return false
    }

    override fun onInit(savedInstanceState: Bundle?, msg: Msg) {
        savedInstanceState?.getParcelable<S>(bundleKey)?.let {
            actionExecutor.state = it
            actionExecutor.render()
        }
        if (actionExecutor.state == null) {
            actionExecutor.state = initialState
            actionExecutor.accept(msg)
        }
    }

    override fun onAttachTo(view: V) {
        this.viewWeakRef = WeakReference(view)
    }

    override fun onStart() {}
    override fun onStop() {}

    override fun onDetach() {
        actionExecutor.clearQueue()
        viewWeakRef?.clear()
        viewWeakRef = null
    }

    open fun accept(msg: Msg) {
        actionExecutor.accept(msg)
    }

    override fun onSaveState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(bundleKey, actionExecutor.state)
    }
}