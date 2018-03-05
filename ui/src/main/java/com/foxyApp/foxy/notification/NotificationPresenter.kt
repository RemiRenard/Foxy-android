package com.foxyApp.foxy.notification

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.foxyApp.data.model.Notification
import com.foxyApp.data.network.apiResponse.SimpleSuccessResponse
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.foxy.notification.dagger.NotificationScope

/**
 * Notification presenter
 */
@NotificationScope
class NotificationPresenter(private val mContext: Context, private val mNotificationService: INotificationService) : INotificationPresenter {

    private var mView: INotificationView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: INotificationView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getNotifications(forceNetworkRefresh: Boolean) {
        mView?.showProgressBar()
        mNotificationService.getNotifications(forceNetworkRefresh)
                .subscribe(object : Observer<List<Notification>> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull notifications: List<Notification>) {
                        mView?.displayNotifications(notifications)
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

    override fun markNotificationAsRead(notificationId: String) {
        mNotificationService.markNotificationAsRead(notificationId)
                .subscribe(object : Observer<SimpleSuccessResponse> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull simpleSuccessResponse: SimpleSuccessResponse) {
                        // @TODO Do something
                    }

                    override fun onError(@NonNull e: Throwable) {
                        Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                    }
                })
    }
}
