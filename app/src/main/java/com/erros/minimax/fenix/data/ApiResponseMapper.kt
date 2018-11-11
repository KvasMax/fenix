package com.erros.minimax.fenix.data

import retrofit2.adapter.rxjava2.Result

object ApiResponseMapper {

    fun <R> map(result: Result<R>): R {
        if (result.isError) {
            throw result.error()
        } else {
            return result.response()?.body() ?: throw Exception("Response is null")
        }
    }

}