package com.erros.minimax.fenix.view.base

/**
 * Created by minimax on 5/1/18.
 */
interface BaseView<Self : BaseView<Self, P>, P : Presenter<P, Self>> {

    val layoutId: Int

    val presenter: P

}