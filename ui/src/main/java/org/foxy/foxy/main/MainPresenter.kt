package org.foxy.foxy.main

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.main.dagger.MainScope

/**
 * Main presenter
 */
@MainScope
class MainPresenter(private val mContext: Context, private val mUserService: IUserService) : IMainPresenter {

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
}
