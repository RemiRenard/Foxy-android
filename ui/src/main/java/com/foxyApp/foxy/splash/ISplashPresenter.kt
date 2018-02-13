package com.foxyApp.foxy.splash

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the splash presenter.
 */
interface ISplashPresenter : IPresenter<ISplashView> {

    fun setSessionToken()

    fun getSessionToken(): String
}
