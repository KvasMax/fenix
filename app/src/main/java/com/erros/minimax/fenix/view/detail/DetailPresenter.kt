package com.erros.minimax.fenix.view.detail

import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.PlaceholderRepository
import com.erros.minimax.fenix.data.Post
import com.erros.minimax.fenix.data.ResourceProvider
import com.erros.minimax.fenix.view.base.*
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize

class DetailPresenter constructor(
        private val placeholderRepository: PlaceholderRepository,
        private val resourceProvider: ResourceProvider
) : BasePresenter<DetailPresenter, DetailView, DetailPresenter.DetailState>(),
        RxActionExecutor.RxComponent<DetailPresenter.DetailState> {

    // place a state and fields at the top

    @Parcelize
    data class DetailState(
            val userId: Int? = null,
            val posts: List<Post>? = null,
            val isLoading: Boolean = false,
            val isRefreshing: Boolean = false
    ) : State()

    override val initialState: DetailState = DetailPresenter.DetailState()

    override val actionExecutor: ActionExecutor<DetailState> = RxActionExecutor(this)

    override fun call(command: Command): Single<Msg> {
        return when (command) {
            is GetPostsCommand -> placeholderRepository.getPostsForUser(command.userId).map { PostsMsg(it) }
            is ShowErrorMessageCommand -> doOnUIThread { updateView { showError(command.msg) } }
            else -> Single.just(Idle)
        }
    }

    override fun update(msg: Msg, state: DetailPresenter.DetailState): Pair<DetailPresenter.DetailState, Command> {
        return when (msg) {
            is UserIdMsg -> state.copy(isLoading = true, userId = msg.userId) to GetPostsCommand(userId = msg.userId)
            is Refresh -> state.copy(isRefreshing = true) to (state.userId?.let { GetPostsCommand(it) } ?: None)
            is PostsMsg -> state.copy(posts = msg.posts, isLoading = false, isRefreshing = false) to None
            // ignoring error type for now
            is ErrorMsg -> state.copy(isLoading = false, isRefreshing = false) to ShowErrorMessageCommand(resourceProvider.getString(R.string.unexpected_error))
            else -> state to None
        }
    }

    override fun render(state: DetailPresenter.DetailState) {
        updateView {
            setProgressVisibility(state.isLoading)
            setRefreshVisibility(state.isRefreshing)
            state.posts?.let {
                showList(it)
            }
        }
    }

    //place commands and messages at the bottom

    //Commands
    data class GetPostsCommand(
            val userId: Int
    ) : Command()

    //Messages
    data class UserIdMsg(
            val userId: Int
    ) : Msg()

    data class PostsMsg(
            val posts: List<Post>
    ) : Msg()

}