package com.foxyApp.foxy.notification.add

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import com.foxyApp.data.model.Notification
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.foxy.notification.dagger.NotificationScope
import java.io.File

/**
 * Add notification presenter
 */
@NotificationScope
class AddNotificationPresenter(private val mNotificationService: INotificationService,
                               private val mContext: Context) : IAddNotificationPresenter {

    private var mView: IAddNotificationView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IAddNotificationView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun saveTmpNotification(message: String, keyword: String, type: String, song: String, audioFile: File?) {
        mView?.showProgressBar()
        mNotificationService.saveTmpNotification(Notification(message, keyword, type, song), audioFile).doOnComplete {
            mView?.hideProgressBar()
            mView?.openFriendsActivity()
        }.subscribe()
    }
}
