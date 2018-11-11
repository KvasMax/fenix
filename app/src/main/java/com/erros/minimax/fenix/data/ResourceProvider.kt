package com.erros.minimax.fenix.data

import android.content.Context
import android.support.annotation.StringRes

class ResourceProvider(private val context: Context) {

    fun getString(@StringRes id: Int): String = context.getString(id)

    fun getString(@StringRes id: Int, varargs: Any?): String = context.getString(id, varargs)
}
