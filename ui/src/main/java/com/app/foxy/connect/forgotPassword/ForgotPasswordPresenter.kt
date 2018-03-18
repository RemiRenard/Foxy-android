package com.app.foxy.connect.forgotPassword

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.app.data.network.ExceptionHandler
import com.app.data.network.apiResponse.SimpleSuccessResponse
import com.app.domain.services.user.IUserService
import com.app.foxy.R
import com.app.foxy.connect.dagger.ConnectScope

/**
 * Forgot password presenter
 */
@ConnectScope
class ForgotPasswordPresenter(private val mContext: Context,
                              private val mUserService: IUserService) : IForgotPasswordPresenter {

    private var mView: IForgotPasswordView? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun attachView(view: IForgotPasswordView) {
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable.clear()
        mView = null
    }

    override fun forgotPassword(email: String) {
        mView?.showProgressBar()
        mUserService.forgotPassword(email)
                .subscribe(object : Observer<SimpleSuccessResponse> {
                    override fun onComplete() {
                        mView?.hideProgressBar()
                        mView?.enableButton(true)
                    }

                    override fun onError(e: Throwable) {
                        mView?.hideProgressBar()
                        mView?.enableButton(true)
                        Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mCompositeDisposable.add(d)
                    }

                    override fun onNext(response: SimpleSuccessResponse) {
                        if (response.success) {
                            Toast.makeText(mContext, R.string.email_reset_password_sent, Toast.LENGTH_LONG).show()
                            mView?.resetPasswordComplete()
                        }
                    }
                })
    }
}