package ru.mikhailskiy.intensiv.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val background = Schedulers.io()
val main = AndroidSchedulers.mainThread()

fun <T> Single<T>.init(): Single<T> {
    return this.subscribeOn(background)
        .observeOn(main)
}

fun <T> Observable<T>.init(): Observable<T> {
    return this.subscribeOn(background)
        .observeOn(main)
}

fun Completable.init(): Completable {
    return this.subscribeOn(background)
        .observeOn(main)
}