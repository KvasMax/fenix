package com.erros.minimax.fenix.view.detail

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.Post
import com.erros.minimax.fenix.di.Scopes
import com.erros.minimax.fenix.view.base.BaseActivity
import com.erros.minimax.fenix.view.base.Init
import com.erros.minimax.fenix.view.base.Msg
import com.erros.minimax.fenix.view.base.Refresh
import com.erros.minimax.fenix.view.utils.bundleOf
import com.erros.minimax.fenix.view.utils.launchActivity
import com.erros.minimax.fenix.view.utils.showErrorMessage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_post.view.*
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope

class DetailActivity : BaseActivity<DetailView, DetailPresenter>(), DetailView {

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"
        fun start(activity: Activity, userId: Int) {
            activity.launchActivity<DetailActivity>(bundleOf(ARG_USER_ID to userId))
        }
    }

    init {
        bindScope(getKoin().getOrCreateScope(Scopes.DETAIL_SCOPE))
    }

    override val presenter: DetailPresenter by inject()

    override val layoutId: Int
        get() = R.layout.activity_detail

    override val initialMsg: Msg
        get() = intent.extras?.let {
            DetailPresenter.UserIdMsg(it.getInt(ARG_USER_ID))
        } ?: Init

    private val adapter = PostsAdapter()

    override fun onViewCreated() {
        title = "Posts"
        swipeRefresh.setOnRefreshListener { presenter.accept(Refresh) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun setProgressVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun showError(msg: String) {
        showErrorMessage(msg)
    }

    override fun setRefreshVisibility(visible: Boolean) {
        swipeRefresh.isRefreshing = visible
    }

    override fun showList(posts: List<Post>) {
        adapter.setCollection(posts)
    }

    inner class PostsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val posts = mutableListOf<Post>()

        fun setCollection(posts: List<Post>) {
            this.posts.clear()
            this.posts.addAll(posts)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PostViewHolder).bind(posts[position])
        }

        override fun getItemCount(): Int = posts.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        }

        inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(post: Post) {
                itemView.postTextView.text = post.body
            }

        }
    }
}
