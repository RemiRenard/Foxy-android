package org.foxy.foxy.notification.select_friends

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.model.User
import org.foxy.data.network.ExceptionHandler
import org.foxy.data.network.api_response.SimpleSuccessResponse
import org.foxy.domain.services.friend.IFriendService
import org.foxy.domain.services.notification.INotificationService
import org.foxy.foxy.R
import org.foxy.foxy.notification.dagger.NotificationScope
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
