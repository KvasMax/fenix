package com.erros.minimax.fenix.data

import com.erros.minimax.fenix.view.utils.toByteArray
import com.erros.minimax.fenix.view.utils.toParcelable
import io.reactivex.Single


/**
 * Created by milkman on 06.02.18.
 */
class PlaceholderRepository constructor(private val placeholderApi: PlaceholderApi) {

    fun getUsers(): Single<List<Person>> = placeholderApi.getUsers().map {
        val oldList = ApiResponseMapper.map(it)
        val oldListSize = oldList.size
        val newListSize = 10000
        val newList = ArrayList<Person>(newListSize)
        for (index in 0 until newListSize) {
            newList.add(oldList[index % oldListSize].toByteArray().toParcelable(Person.CREATOR)!!)
        }
        return@map newList
    }

    fun getPostsForUser(userId: Int): Single<List<Post>> = placeholderApi.getPostsForUser(userId).map { ApiResponseMapper.map(it) }

}