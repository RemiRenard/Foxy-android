package com.foxyApp.foxy.notification.selectFriends

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.foxyApp.data.model.User
import com.foxyApp.data.network.ExceptionHandler
import com.foxyApp.data.network.apiResponse.SimpleSuccessResponse
import com.foxyApp.domain.services.friend.IFriendService
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.foxy.notification.dagger.NotificationScope
import java.io.File

/**
 * Friends presenter
 */
@NotificationScope
class SelectFriendsPresenter(private val mContext: Context, private val mFriendService: IFriendService,
                             private val mNotificationService: INotificationService) : ISelectFriendsPresenter {

    private var mView: ISelectFriendsView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: ISelectFriendsView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getFriends() {
        mView?.showProgressBar()
        mFriendService.getFriends().subscribe(object : Observer<List<User>> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull friends: List<User>) {
                mView?.displayFriends(friends)
            }

            override fun onError(@NonNull e: Throwable) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
                mView?.hideProgressBar()
            }

            override fun onComplete() {
                mView?.hideProgressBar()
            }
        })
    }

    override fun sendNotification(userIds: List<String>) {
        mView?.showProgressBar()
        mView?.enableButton(false)
        mNotificationService.sendNotification(userIds)
                .subscribe(object : Observer<SimpleSuccessResponse> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull notifSentResponse: SimpleSuccessResponse) {
                        // Do nothing here.
                    }

                    override fun onError(@NonNull e: Throwable) {
                        e.printStackTrace()
                        Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_SHORT).show()
                        mView?.hideProgressBar()
                        mView?.enableButton(true)
                    }

                    override fun onComplete() {
                        File(mContext.externalCacheDir.absolutePath + "/foxyAudioRecord.mp3").delete()
                        mView?.notificationSent()
                        mView?.enableButton(true)
                    }
                })
    }

    override fun clearNotificationCache()
    {
        mNotificationService.clearNotificationCache()
    }
}
