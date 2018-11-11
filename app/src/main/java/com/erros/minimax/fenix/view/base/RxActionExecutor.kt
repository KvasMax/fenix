package com.erros.minimax.fenix.view.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class RxActionExecutor<S : State>(
        private val rxComponent: RxComponent<S>
) : ActionExecutor<S>(rxComponent) {

    private val compositeDisposable = CompositeDisposable()

    override fun processCommand(command: Command, callback: (Msg) -> Unit) {
        compositeDisposable.add(
                rxComponent.call(command)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            callback.invoke(it)
                        }, {
                            callback.invoke(CommandErrorMsg(it, command))
                        })
        )
    }

    override fun clearQueue() {
        super.clearQueue()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    interface RxComponent<S> : ActionExecutor.Component<S> {

        fun call(command: Command): Single<Msg>

        @Deprecated("Not used")
        override fun call(command: Command, callback: (Msg) -> Unit) {

        }
    }
}

inline fun doOnUIThread(crossinline operations: () -> Unit): Single<Msg> {
    return Single.fromCallable {
        operations()
    }.subscribeOn(AndroidSchedulers.mainThread()).map { Idle }
}