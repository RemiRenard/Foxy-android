package com.app.foxy.connect

import android.content.Context
import android.widget.Toast
import com.facebook.login.LoginResult
import com.app.data.network.ExceptionHandler
import com.app.data.network.apiResponse.ConnectResponse
import com.app.domain.services.user.IUserService
import com.app.foxy.R
import com.app.foxy.connect.dagger.ConnectScope
import com.app.foxy.eventBus.ConnectStepCompleteEvent
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Connect presenter
 */
@ConnectScope
class ConnectPresenter(private val mContext: Context, private val mUserService: IUserService) : IConnectPresenter {

    private var mView: IConnectView? = null
    private var mToken: String? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun attachView(view: IConnectView) {
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable.clear()
        mView = null
    }

    override fun loginFacebook(loginResult: LoginResult) {
        mView?.showProgressBar()
        mUserService.loginFacebook(loginResult.accessToken.userId, loginResult.accessToken.token)
                .subscribe(object : Observer<ConnectResponse> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable.add(d)
                    }

                    override fun onNext(@NonNull connectResponse: ConnectResponse) {
                        if (connectResponse.token != null) {
                            mToken = connectResponse.token
                            mUserService.setToken(mToken, mContext)
                            mUserService.saveCurrentUser(connectResponse.user)
                        }
                    }

                    override fun onError(@NonNull e: Throwable) {
                        Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                        mView?.hideProgressBar()
                        mView?.enableButtons(true)
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                        mView?.enableButtons(true)
                        if (mToken != null) {
                            EventBus.getDefault().post(ConnectStepCompleteEvent())
                        }
                    }
                })
    }

    override fun login(email: String, password: String) {
        mView?.showProgressBar()
        mUserService.login(email, password)
                .subscribe(object : Observer<ConnectResponse> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable.add(d)
                    }

                    override fun onNext(@NonNull connectResponse: ConnectResponse) {
                        if (connectResponse.token != null) {
                            mToken = connectResponse.token
                            mUserService.setToken(mToken, mContext)
                            mUserService.saveCurrentUser(connectResponse.user)
                        }
                    }

                    override fun onError(@NonNull e: Throwable) {
                        Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                        mView?.hideProgressBar()
                        mView?.enableButtons(true)
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                        mView?.enableButtons(true)
                        if (mToken != null) {
                            EventBus.getDefault().post(ConnectStepCompleteEvent())
                        }
                    }
                })
    }

    override fun createAccount(email: String, password: String, firstName: String, lastName: String,
                               username: String, birthday: Date) {
        mView?.showProgressBar()
        mUserService.createAccount(email, password, firstName, lastName, username, birthday)
                .subscribe(object : Observer<ConnectResponse> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable.add(d)
                    }

                    override fun onNext(@NonNull connectResponse: ConnectResponse) {
                        if (connectResponse.token != null) {
                            mToken = connectResponse.token
                            mUserService.setToken(mToken, mContext)
                            mUserService.saveCurrentUser(connectResponse.user)
                            Toast.makeText(mContext, R.string.email_confirmation_sent, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(@NonNull e: Throwable) {
                        Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                        mView?.hideProgressBar()
                        mView?.enableButtons(true)
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                        mView?.enableButtons(true)
                        if (mToken != null) {
                            EventBus.getDefault().post(ConnectStepCompleteEvent())
                        }
                    }
                })
    }
}