package com.erros.minimax.fenix.data

import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by milkman on 07.02.18.
 */
interface PlaceholderApi {

    @GET("/users")
    fun getUsers(): Single<Result<List<Person>>>

    @GET("/posts")
    fun getPostsForUser(@Query("userId") userId: Int): Single<Result<List<Post>>>
}

