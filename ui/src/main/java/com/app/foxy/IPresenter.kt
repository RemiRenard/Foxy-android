package com.app.foxy

/**
 * The basic interface for a presenter. All presenters should implement it.
 */
interface IPresenter<in T> {

    fun attachView(view: T)

    fun detachView()
}