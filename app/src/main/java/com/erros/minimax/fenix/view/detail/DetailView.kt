package com.erros.minimax.fenix.view.detail

import com.erros.minimax.fenix.data.Post
import com.erros.minimax.fenix.view.base.BaseView

interface DetailView : BaseView<DetailView, DetailPresenter> {
    fun changeProgressVisibility(visible: Boolean)
    fun showList(posts: List<Post>)
    fun changeRefreshVisibility(visible: Boolean)
}