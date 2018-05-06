package com.erros.minimax.fenix.view.detail

import android.annotation.SuppressLint
import com.erros.minimax.fenix.data.PlaceholderRepository
import com.erros.minimax.fenix.data.Post
import com.erros.minimax.fenix.view.base.*
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class DetailPresenter @Inject constructor(
        private val placeholderRepository: PlaceholderRepository
) : BasePresenter<DetailPresenter, DetailView, DetailPresenter.DetailState>(), RxActionScheduler.RxComponent<DetailPresenter.DetailState> {

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class DetailState(
            val userId: Int? = null,
            val posts: List<Post>? = null,
            val isLoading: Boolean = false,
            val isRefreshing: Boolean = false
    ) : State()

    //Commands
    data class GetPostsCmd(
            val userId: Int
    ) : Cmd()

    //Messages
    data class UserIdMsg(
            val userId: Int
    ) : Msg()

    data class PostsMsg(
            val posts: List<Post>
    ) : Msg()

    class Refresh : Msg()

    private val rxActionScheduler = RxActionScheduler(this)

    override fun initialState(): DetailPresenter.DetailState = DetailPresenter.DetailState()

    fun accept(msg: Msg) {
        rxActionScheduler.accept(msg)
    }

    override fun call(cmd: Cmd): Single<Msg> {
        return when (cmd) {
            is GetPostsCmd -> placeholderRepository.getPostsForUser(cmd.userId).map { PostsMsg(it) }
            else -> Single.just(Idle)
        }
    }

    override fun update(msg: Msg, state: DetailPresenter.DetailState): Pair<DetailPresenter.DetailState, Cmd> {
        return when (msg) {
            is UserIdMsg -> Pair(state.copy(isLoading = true, userId = msg.userId), GetPostsCmd(userId = msg.userId))
            is Refresh -> Pair(state.copy(isRefreshing = true), state.userId?.let { GetPostsCmd(it) }
                    ?: None)
            is PostsMsg -> Pair(state.copy(posts = msg.posts, isLoading = false, isRefreshing = false), None)
            else -> Pair(state, None)
        }
    }

    override fun render(state: DetailPresenter.DetailState) {
        view?.let { view ->
            view.changeProgressVisibility(state.isLoading)
            view.changeRefreshVisibility(state.isRefreshing)
            state.posts?.let {
                view.showList(it)
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        rxActionScheduler.dispose()
    }

    override val actionScheduler: ActionScheduler<DetailPresenter.DetailState>
        get() = rxActionScheduler
}