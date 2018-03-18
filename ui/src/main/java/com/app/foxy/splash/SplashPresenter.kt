package com.app.foxy.splash

import android.content.Context
import com.app.data.Constants
import com.app.data.model.Config
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.user.IUserService
import com.app.foxy.BuildConfig
import com.app.foxy.splash.dagger.SplashScope
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Splash presenter
 */
@SplashScope
class SplashPresenter(private val mUserService: IUserService, private val mGlobalService: IGlobalService,
                      private val mContext: Context) : ISplashPresenter {

    private val mCompositeDisposable = CompositeDisposable()
    private var mView: ISplashView? = null

    override fun attachView(view: ISplashView) {
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable.clear()
        mView = null
    }

    override fun setSessionToken() {
        mUserService.setToken(mContext.getSharedPreferences(Constants.APP_TOKEN_PREF, Context.MODE_PRIVATE)
                .getString(Constants.TOKEN, null), mContext)
    }

    override fun getSessionToken(): String = mUserService.getToken()

    override fun checkConfig() {
        mGlobalService.getApiConfig().subscribe(object : Observer<Config> {
            override fun onComplete() {
                // Do nothing.
            }

            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onNext(config: Config) {
                mView?.forceUpdate(BuildConfig.VERSION_CODE >= config.minAndroidVersion!!)
            }

            override fun onError(e: Throwable) {
                mView?.forceUpdate(true)
            }
        })
    }
}
