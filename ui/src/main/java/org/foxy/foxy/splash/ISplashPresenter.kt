package org.foxy.foxy.splash

import org.foxy.foxy.IPresenter

/**
 * Interface of the splash presenter.
 */
interface ISplashPresenter : IPresenter<ISplashView> {

    fun setSessionToken()

    fun getSessionToken(): String
}
