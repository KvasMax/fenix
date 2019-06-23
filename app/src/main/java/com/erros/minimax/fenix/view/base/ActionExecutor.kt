package com.erros.minimax.fenix.view.base

import android.os.Parcelable
import java.util.*

sealed class AbstractState
abstract class State : AbstractState(), Parcelable

sealed class AbstractMsg
open class Msg : AbstractMsg()
object Idle : Msg()
object Init : Msg()
object Refresh : Msg()
open class ErrorMsg(val error: Throwable, val command: Command) : Msg()
class RenderErrorMsg(error: Throwable, command: Command) : ErrorMsg(error, command)
class CommandErrorMsg(error: Throwable, command: Command) : ErrorMsg(error, command)

sealed class AbstractCommand
open class Command : AbstractCommand()
object None : Command()
class ShowErrorMessageCommand(val msg: String) : Command()

open class ActionExecutor<S : State>(
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
                val (state, command) = component.update(msgQueue.removeFirst(), it)
                this.state = state
                try {
                    component.render(state)
                    if (command !is None) {
                        try {
                            processCommand(command) { msg ->
                                when (msg) {
                                    is Idle -> {
                                    }
                                    else -> msgQueue.addLast(msg)
                                }
                                loop()
                            }
                        } catch (ex: Throwable) {
                            msgQueue.addLast(CommandErrorMsg(ex, command))
                            loop()
                        }
                    }
                } catch (ex: Throwable) {
                    msgQueue.addLast(RenderErrorMsg(ex, command))
                    loop()
                }
            }
        }
    }

    protected open fun processCommand(command: Command, callback: (Msg) -> Unit) {
        component.call(command) { msg ->
            callback.invoke(msg)
        }
    }

    fun accept(msg: Msg) {
        msgQueue.addLast(msg)
        if (msgQueue.size == 1) {
            loop()
        }
    }

    open fun clearQueue() {
        msgQueue.clear()
    }

    interface Component<S> {

        fun update(msg: Msg, state: S): Pair<S, Command>

        fun render(state: S)

        fun call(command: Command, callback: (Msg) -> Unit)
    }

}