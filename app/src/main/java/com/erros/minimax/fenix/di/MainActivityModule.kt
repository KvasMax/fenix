package com.erros.minimax.fenix.di

import com.erros.minimax.fenix.view.main.MainPresenter
import toothpick.config.Module

/**
 * Created by minimax on 1/28/18.
 */
class MainActivityModule : Module() {

    init {
        bind(MainPresenter::class.java)
    }

}