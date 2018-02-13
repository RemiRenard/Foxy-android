package com.foxyApp.foxy.splash

import android.content.Context
import com.foxyApp.data.Constants
import com.foxyApp.domain.Domain
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.splash.dagger.SplashScope

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
