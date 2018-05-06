package com.erros.minimax.fenix.view.main

import android.annotation.SuppressLint
import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.data.PlaceholderRepository
import com.erros.minimax.fenix.view.base.*
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

/**
 * Created by minimax on 5/1/18.
 */
class MainPresenter @Inject constructor(
        private val placeholderRepository: PlaceholderRepository
) : BasePresenter<MainPresenter, MainView, MainPresenter.MainState>(), RxActionScheduler.RxComponent<MainPresenter.MainState> {

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class MainState(
            val persons: List<Person>? = null,
            val isLoading: Boolean = false,
            val isRefreshing: Boolean = false
    ) : State()

    class GetPersonsCmd : Cmd()

    data class PersonsMsg(
            val persons: List<Person>
    ) : Msg()

    class Refresh : Msg()

    private val rxActionScheduler = RxActionScheduler(this)

    override fun initialState(): MainState = MainState()

    fun onRefresh() {
        rxActionScheduler.accept(Refresh())
    }

    override fun call(cmd: Cmd): Single<Msg> {
        return when (cmd) {
            is GetPersonsCmd -> placeholderRepository.getUsers().map { PersonsMsg(it) }
            else -> Single.just(Idle)
        }
    }

    override fun update(msg: Msg, state: MainState): Pair<MainState, Cmd> {
        return when (msg) {
            is Init -> Pair(state.copy(isLoading = true), GetPersonsCmd())
            is Refresh -> Pair(state.copy(isRefreshing = true), GetPersonsCmd())
            is PersonsMsg -> Pair(state.copy(persons = msg.persons, isLoading = false, isRefreshing = false), None)
            else -> Pair(state, None)
        }
    }

    override fun render(state: MainState) {
        view?.let { view ->
            view.changeProgressVisibility(state.isLoading)
            view.changeRefreshVisibility(state.isRefreshing)
            state.persons?.let {
                view.showList(it)
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        rxActionScheduler.dispose()
    }

    fun onPersonClick(person: Person) {
        view?.showPostsForUser(person.id)
    }

    override val actionScheduler: ActionScheduler<MainState>
        get() = rxActionScheduler

}