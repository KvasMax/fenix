package com.erros.minimax.fenix.data

import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by milkman on 06.02.18.
 */
class PlaceholderRepository
@Inject constructor(private val placeholderApi: PlaceholderApi) {

    fun getUsers() = placeholderApi.getUsers().delay(1, TimeUnit.SECONDS).map { it.response().body() }

    fun getPostsForUser(userId: Int) = placeholderApi.getPostsForUser(userId).delay(1, TimeUnit.SECONDS).map { it.response().body() }
}