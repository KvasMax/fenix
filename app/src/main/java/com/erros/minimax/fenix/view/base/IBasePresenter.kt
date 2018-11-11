package com.erros.minimax.fenix.view.base

import android.os.Bundle

/**
 * Created by minimax on 5/1/18.
 */
interface IBasePresenter<Self : IBasePresenter<Self, V>, V : BaseView<V, Self>> {

    fun onInit(savedInstanceState: Bundle?, msg: Msg)

    fun onAttachTo(view: V)

    fun onSaveState(savedInstanceState: Bundle)

    fun onStart()

    fun onStop()

    fun onDetach()
}