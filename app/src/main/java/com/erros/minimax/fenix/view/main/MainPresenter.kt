package com.erros.minimax.fenix.view.main

import android.os.Parcel
import android.os.Parcelable
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.data.PlaceholderRepository
import com.erros.minimax.fenix.data.ResourceProvider
import com.erros.minimax.fenix.data.StateStorage
import com.erros.minimax.fenix.view.base.*
import io.reactivex.Single

/**
 * Created by minimax on 5/1/18.
 */
class MainPresenter constructor(
        private val placeholderRepository: PlaceholderRepository,
        private val resourceProvider: ResourceProvider,
        stateStorage: StateStorage
) : StatePresenter<MainPresenter, MainView, MainPresenter.MainState>(stateStorage),
        RxActionExecutor.RxComponent<MainPresenter.MainState> {

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

    //place a state, commands and messages at the bottom

    override val parcelableCreator: Parcelable.Creator<MainState> = MainState.CREATOR

    data class MainState(
            val persons: List<Person>? = null,
            val isLoading: Boolean = false,
            val isRefreshing: Boolean = false,
            val chosenPerson: Person? = null
    ) : State(), Parcelable {
        constructor(source: Parcel) : this(
                source.createTypedArrayList(Person.CREATOR),
                1 == source.readInt(),
                1 == source.readInt(),
                source.readParcelable<Person>(Person::class.java.classLoader)
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeTypedList(persons)
            writeInt((if (isLoading) 1 else 0))
            writeInt((if (isRefreshing) 1 else 0))
            writeParcelable(chosenPerson, 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<MainState> = object : Parcelable.Creator<MainState> {
                override fun createFromParcel(source: Parcel): MainState = MainState(source)
                override fun newArray(size: Int): Array<MainState?> = arrayOfNulls(size)
            }
        }

    }

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