package com.erros.minimax.fenix.data

import io.reactivex.Single


/**
 * Created by milkman on 06.02.18.
 */
class PlaceholderRepository constructor(private val placeholderApi: PlaceholderApi) {

    fun getUsers(): Single<List<Person>> = placeholderApi.getUsers().map { ApiResponseMapper.map(it) }

    fun getPostsForUser(userId: Int): Single<List<Post>> = placeholderApi.getPostsForUser(userId).map { ApiResponseMapper.map(it) }

}