package com.erros.minimax.fenix.view.base

import android.os.Parcelable
import java.util.*


sealed class AbstractState
abstract class State : AbstractState(), Parcelable

sealed class AbstractMsg
open class Msg : AbstractMsg()
object Idle : Msg()
object Init : Msg()
class ErrorMsg(val err: Throwable, val cmd: Cmd) : Msg()

sealed class AbstractCmd
open class Cmd : AbstractCmd()
object None : Cmd()

open class ActionScheduler<S : State>(
        private val component: Component<S>
) {

    var state: S? = null
    private var msgQueue = ArrayDeque<Msg>()

    fun render() {
        state?.let { component.render(it) }
    }

    private fun loop() {
        if (msgQueue.size > 0) {
            state?.let {
                val updateResult = component.update(msgQueue.removeFirst(), it)
                this.state = updateResult.first
                component.render(updateResult.first)
                if (updateResult.second !is None) {
                    processCommand(updateResult.second, { msg ->
                        when (msg) {
                            is Idle -> {
                            }
                            else -> msgQueue.addLast(msg)
                        }
                        loop()
                    })
                }
            }
        }
    }

    open protected fun processCommand(cmd: Cmd, callback: (Msg) -> Unit) {
        component.call(cmd, { msg ->
            callback.invoke(msg)
        })
    }

    fun accept(msg: Msg) {
        msgQueue.addLast(msg)
        if (msgQueue.size == 1) {
            loop()
        }
    }

    interface Component<S> {

        fun update(msg: Msg, state: S): Pair<S, Cmd>

        fun render(state: S)

        fun call(cmd: Cmd, callback: (Msg) -> Unit)
    }

}