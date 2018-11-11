package com.erros.minimax.fenix.view.main

import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.view.base.BaseView
import com.erros.minimax.fenix.view.base.ErrorListener
import com.erros.minimax.fenix.view.base.ProgressListener
import com.erros.minimax.fenix.view.base.RefreshListener

/**
 * Created by minimax on 5/1/18.
 */
interface MainView : BaseView<MainView, MainPresenter>,
        ProgressListener,
        ErrorListener,
        RefreshListener {

    fun showPersons(persons: List<Person>)
    fun openPostsForUser(personId: Int)
    fun markPersonAsChosen(person: Person)

}