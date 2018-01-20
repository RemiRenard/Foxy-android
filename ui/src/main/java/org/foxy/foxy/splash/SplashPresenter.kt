package org.foxy.foxy.splash

import android.content.Context
import org.foxy.data.Constants
import org.foxy.domain.Domain
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.splash.dagger.SplashScope

/**
 * Splash presenter
 */
@SplashScope
class SplashPresenter(private val mUserService: IUserService, private val mContext: Context) : ISplashPresenter {

    private var mView: ISplashView? = null

    override fun attachView(view: ISplashView) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun setSessionToken() {
        mUserService.setToken(mContext.getSharedPreferences(Constants.APP_TOKEN_PREF, Context.MODE_PRIVATE)
                .getString(Constants.TOKEN, null), mContext)
    }

    override fun getSessionToken(): String = mUserService.getToken()
}
