package com.foxyApp.foxy.profile.settings

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.foxyApp.data.network.ExceptionHandler
import com.foxyApp.data.network.api_response.SimpleSuccessResponse
import com.foxyApp.domain.Domain
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.profile.dagger.ProfileScope

/**
 * Settings presenter
 */
@ProfileScope
class SettingsPresenter(private val mUserService: IUserService, private val mContext: Context) : ISettingsPresenter {

    private var mView: ISettingsView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: ISettingsView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun logout() {
        mView?.enableLogoutButton(false)
        mView?.showProgressBar()
        mUserService.logout(mContext)
                .subscribe(object : Observer<SimpleSuccessResponse> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull simpleSuccessResponse: SimpleSuccessResponse) {
                        // Do nothing
                    }

                    override fun onError(@NonNull e: Throwable) {
                        Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                        mView?.enableLogoutButton(true)
                        mView?.hideProgressBar()
                    }

                    override fun onComplete() {
                        mView?.enableLogoutButton(true)
                        mView?.hideProgressBar()
                        mView?.logoutComplete()
                    }
                })

    }
}
