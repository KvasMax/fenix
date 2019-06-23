package com.erros.minimax.fenix.view.base

/**
 * Created by minimax on 5/1/18.
 */
interface Presenter<Self : Presenter<Self, V>, V : BaseView<V, Self>> {

    fun onAttachTo(view: V)

    fun onStart()

    fun onStop()

    fun onDetach()
}