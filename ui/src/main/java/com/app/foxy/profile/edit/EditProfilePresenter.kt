package com.app.foxy.profile.edit

import android.content.Context
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.app.data.model.User
import com.app.domain.services.user.IUserService
import com.app.foxy.profile.dagger.ProfileScope

/**
 * Edit Profile presenter
 */
@ProfileScope
class EditProfilePresenter(private val context: Context, private val mUserService: IUserService) : IEditProfilePresenter {

    private var mView: IEditProfileView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IEditProfileView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getProfile(forceNetworkRefresh: Boolean) {
        mView?.showProgressBar()
        mUserService.getCurrentUser(forceNetworkRefresh)
                .subscribe(object : Observer<User> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull user: User) {
                        mView?.showProfileInformation(user)
                    }

                    override fun onError(@NonNull e: Throwable) {
                        mView?.hideProgressBar()
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                    }
                })
    }
}
