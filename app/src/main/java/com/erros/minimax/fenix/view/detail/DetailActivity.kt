package com.erros.minimax.fenix.view.detail

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.Post
import com.erros.minimax.fenix.di.DetailActivityModule
import com.erros.minimax.fenix.di.Scopes
import com.erros.minimax.fenix.view.base.BaseActivity
import com.erros.minimax.fenix.view.base.Init
import com.erros.minimax.fenix.view.base.Msg
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_post.view.*
import toothpick.Toothpick
import javax.inject.Inject

class DetailActivity : BaseActivity<DetailView, DetailPresenter>(), DetailView {

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"
        fun start(activity: Activity, userId: Int) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(ARG_USER_ID, userId)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: DetailPresenter

    override val layoutId: Int
        get() = R.layout.activity_detail

    override val firstMsg: Msg
        get() = intent.extras?.let {
            DetailPresenter.UserIdMsg(it.getInt(ARG_USER_ID))
        } ?: Init

    private val adapter = PostsAdapter()

    init {
        Toothpick.openScopes(Scopes.APP, Scopes.DETAIL_SCREEN).apply {
            installModules(DetailActivityModule())
            Toothpick.inject(this@DetailActivity, this)
        }
    }

    override fun getBasePresenter(): DetailPresenter = presenter

    override fun onViewCreated() {
        swipeRefresh.setOnRefreshListener { presenter.accept(DetailPresenter.Refresh()) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun changeProgressVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun changeRefreshVisibility(visible: Boolean) {
        swipeRefresh.isRefreshing = visible
    }

    override fun showList(posts: List<Post>) {
        adapter.setCollection(posts)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        Toothpick.closeScope(Scopes.DETAIL_SCREEN)
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
