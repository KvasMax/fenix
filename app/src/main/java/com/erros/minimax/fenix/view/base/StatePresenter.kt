package com.erros.minimax.fenix.view.base

import android.os.Bundle
import android.os.Parcelable
import com.erros.minimax.fenix.data.StateStorage
import java.lang.ref.WeakReference
import kotlin.random.Random

/**
 * Created by minimax on 5/1/18.
 */

abstract class StatePresenter<Self : StatePresenter<Self, V, S>, V : BaseView<V, Self>, S : State>(
        private val stateStorage: StateStorage
) : Presenter<Self, V> {

    private var viewWeakRef: WeakReference<V>? = null
    private val stateIdBundleKey = "KEY_STATE_ID"

    protected val view: V?
        get() = viewWeakRef?.get()

    protected abstract val initialState: S
    protected abstract val parcelableCreator: Parcelable.Creator<S>
    protected abstract val actionExecutor: ActionExecutor<S>

    protected fun updateView(operations: V.() -> Unit): Boolean {
        view?.let {
            operations.invoke(it)
            return true
        }
        return false
    }

    fun onInit(savedInstanceState: Bundle?, msg: Msg) {
        val stateId = savedInstanceState?.getInt(stateIdBundleKey)
        if (stateId == null) {
            actionExecutor.state = initialState
            actionExecutor.accept(msg)
        } else {
            stateStorage.getStateForId(stateId, parcelableCreator) { state ->
                if (state == null) {
                    actionExecutor.state = initialState
                    actionExecutor.accept(msg)
                } else {
                    actionExecutor.state = state
                    actionExecutor.render()
                }
            }
        }
    }

    fun onSaveState(savedInstanceState: Bundle) {
        val stateId = Random.nextInt()
        savedInstanceState.putInt(stateIdBundleKey, stateId)
        actionExecutor.state?.let { stateStorage.setStateForId(stateId, it) }
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

}