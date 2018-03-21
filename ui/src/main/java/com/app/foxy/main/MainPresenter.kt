package com.app.foxy.main

import android.content.Context
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.user.IUserService
import com.app.foxy.main.dagger.MainScope
import io.reactivex.disposables.CompositeDisposable

/**
 * Main presenter
 */
@MainScope
class MainPresenter(private val mContext: Context, private val mUserService: IUserService,
                    private val mGlobalService: IGlobalService) : IMainPresenter {

    private var mView: IMainView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IMainView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun refreshToken() {
        // We should refresh token
        mUserService.refreshToken(mContext)
    }

    override fun manageTutorial() {
        if (!mGlobalService.isMainTutorialShowed(mContext)) {
            mView?.showTutorial()
            mGlobalService.mainTutorialShowed(mContext)
        }
    }

}
