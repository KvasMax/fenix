package com.erros.minimax.fenix.view.detail

import com.erros.minimax.fenix.data.Post
import com.erros.minimax.fenix.view.base.BaseView
import com.erros.minimax.fenix.view.base.ErrorListener
import com.erros.minimax.fenix.view.base.ProgressListener
import com.erros.minimax.fenix.view.base.RefreshListener

interface DetailView : BaseView<DetailView, DetailPresenter>,
        ProgressListener,
        ErrorListener,
        RefreshListener {

    fun showList(posts: List<Post>)

}