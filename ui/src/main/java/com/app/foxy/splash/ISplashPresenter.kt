package com.app.foxy.splash

import com.app.foxy.IPresenter

/**
 * Interface of the splash presenter.
 */
interface ISplashPresenter : IPresenter<ISplashView> {

    fun setSessionToken()

    fun getSessionToken(): String

    fun checkConfig()
}
