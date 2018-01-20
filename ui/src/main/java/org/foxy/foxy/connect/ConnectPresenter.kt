package org.foxy.foxy.connect

import android.content.Context
import android.widget.Toast
import com.facebook.login.LoginResult
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.Constants
import org.foxy.data.network.ExceptionHandler
import org.foxy.data.network.api_response.ConnectResponse
import org.foxy.domain.Domain
import org.foxy.domain.services.notification.INotificationService
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.R
import org.foxy.foxy.connect.dagger.ConnectScope
import org.foxy.foxy.event_bus.ConnectStepCompleteEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Connect presenter
 */
@ConnectScope
class ConnectPresenter(private val mContext: Context, private val mUserService: IUserService,
                       private val mNotificationService: INotificationService) : IConnectPresenter {

    private var mView: IConnectView? = null
    private var mToken: String? = null
    private var mDeviceId: String? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun attachView(view: IConnectView) {
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable.clear()
        mView = null
    }

    override fun loginFacebook(loginResult: LoginResult) {
        if (mToken != null) {
            EventBus.getDefault().post(ConnectStepCompleteEvent())
        }
    }

    override fun login(email: String, password: String) {
        mView?.showProgressBar()
        mCompositeDisposable.add(mNotificationService.getTokenOnIoThread()
                .doOnComplete {
                    mUserService.login(email, password, mDeviceId!!)
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
                .subscribe({ scope -> mDeviceId = FirebaseInstanceId.getInstance().getToken(Constants.PROJECT_NUMBER, scope) },
                        { error -> error.printStackTrace() }))
    }

    override fun createAccount(email: String, password: String, firstName: String, lastName: String,
                               username: String, birthday: Date) {
        mView?.showProgressBar()
        mCompositeDisposable.add(mNotificationService.getTokenOnIoThread()
                .doOnComplete {
                    mUserService.createAccount(email, password, firstName, lastName, username,
                            birthday, mDeviceId!!)
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
                // Get the deviceID to receive/sent notification
                .subscribe({ scope -> mDeviceId = FirebaseInstanceId.getInstance().getToken(Constants.PROJECT_NUMBER, scope) },
                        { error -> error.printStackTrace() }))
    }
}