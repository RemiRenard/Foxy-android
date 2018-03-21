package com.app.foxy.main

import com.app.foxy.IPresenter

/**
 * Interface of the main presenter.
 */
interface IMainPresenter : IPresenter<IMainView> {

    fun refreshToken()

    fun manageTutorial()
}