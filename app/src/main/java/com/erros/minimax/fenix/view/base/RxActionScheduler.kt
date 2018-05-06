package com.erros.minimax.fenix.view.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class RxActionScheduler<S : State>(
        private val rxComponent: RxComponent<S>
) : ActionScheduler<S>(rxComponent) {

    private val compositeDisposable = CompositeDisposable()

    override fun processCommand(cmd: Cmd, callback: (Msg) -> Unit) {
        compositeDisposable.add(
                rxComponent.call(cmd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            callback.invoke(it)
                        }, {
                            callback.invoke(ErrorMsg(it, cmd))
                        })
        )
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    interface RxComponent<S> : ActionScheduler.Component<S> {

        fun call(cmd: Cmd): Single<Msg>

        @Deprecated("Not used")
        override fun call(cmd: Cmd, callback: (Msg) -> Unit) {

        }
    }
}