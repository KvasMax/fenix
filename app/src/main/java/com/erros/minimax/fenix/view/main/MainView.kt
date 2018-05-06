package com.erros.minimax.fenix.view.main

import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.view.base.BaseView

/**
 * Created by minimax on 5/1/18.
 */
interface MainView : BaseView<MainView, MainPresenter> {
    fun changeProgressVisibility(visible: Boolean)
    fun showList(persons: List<Person>)
    fun changeRefreshVisibility(visible: Boolean)
    fun showPostsForUser(personId: Int)
}