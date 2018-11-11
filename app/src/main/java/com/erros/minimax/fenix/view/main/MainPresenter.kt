package com.erros.minimax.fenix.view.main

import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.data.PlaceholderRepository
import com.erros.minimax.fenix.data.ResourceProvider
import com.erros.minimax.fenix.view.base.*
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize

/**
 * Created by minimax on 5/1/18.
 */
class MainPresenter constructor(
        private val placeholderRepository: PlaceholderRepository,
        private val resourceProvider: ResourceProvider
) : BasePresenter<MainPresenter, MainView, MainPresenter.MainState>(),
        RxActionExecutor.RxComponent<MainPresenter.MainState> {

    // place a state and fields at the top

    @Parcelize
    data class MainState(
            val persons: List<Person>? = null,
            val isLoading: Boolean = false,
            val isRefreshing: Boolean = false,
            val chosenPerson: Person? = null
    ) : State()

    override val initialState: MainState = MainState()

    override val actionExecutor: ActionExecutor<MainState> = RxActionExecutor(this)

    override fun call(command: Command): Single<Msg> {
        return when (command) {
            is GetPersonsCommand -> placeholderRepository.getUsers().map { PersonsMsg(it) }
            is ShowPersonCommand -> doOnUIThread { updateView { openPostsForUser(command.person.id) } }
            is ShowErrorMessageCommand -> doOnUIThread { updateView { showError(command.msg) } }
            else -> Single.just(Idle)
        }
    }

    override fun update(msg: Msg, state: MainState): Pair<MainState, Command> {
        return when (msg) {
            is Init -> state.copy(isLoading = true) to GetPersonsCommand()
            is Refresh -> state.copy(isRefreshing = true) to GetPersonsCommand()
            is PersonsMsg -> state.copy(persons = msg.persons, isLoading = false, isRefreshing = false) to None
            is OnPersonClickMsg -> state.copy(chosenPerson = msg.person) to ShowPersonCommand(msg.person)
            // ignoring error type for now
            is ErrorMsg -> state.copy(isLoading = false, isRefreshing = false) to ShowErrorMessageCommand(resourceProvider.getString(R.string.unexpected_error))
            else -> state to None
        }
    }

    override fun render(state: MainState) {
        updateView {
            setProgressVisibility(state.isLoading)
            setRefreshVisibility(state.isRefreshing)
            state.persons?.let {
                showPersons(it)
            }
            state.chosenPerson?.let {
                markPersonAsChosen(it)
            }
        }
    }

    //place commands and messages at the bottom

    //Commands
    class GetPersonsCommand : Command()

    class ShowPersonCommand(
            val person: Person
    ) : Command()

    //Messages
    data class PersonsMsg(
            val persons: List<Person>
    ) : Msg()

    data class OnPersonClickMsg(
            val person: Person
    ) : Msg()
}