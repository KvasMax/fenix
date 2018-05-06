package com.erros.minimax.fenix.di

import com.erros.minimax.fenix.view.detail.DetailPresenter
import toothpick.config.Module

/**
 * Created by minimax on 1/28/18.
 */
class DetailActivityModule : Module() {

    init {
        bind(DetailPresenter::class.java)
    }

}