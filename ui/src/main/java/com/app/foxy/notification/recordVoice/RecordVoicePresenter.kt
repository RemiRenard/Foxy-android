package com.app.foxy.notification.recordVoice

import com.app.data.model.Notification
import com.app.domain.services.notification.INotificationService
import com.app.foxy.notification.dagger.NotificationScope
import io.reactivex.disposables.CompositeDisposable
import java.io.File

/**
 * Record voice presenter
 */
@NotificationScope
class RecordVoicePresenter(private val mNotificationService: INotificationService) : IRecordVoicePresenter {

    private var mView: IRecordVoiceView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IRecordVoiceView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun saveTmpNotification(message: String, audioFile: File?) {
        mView?.showProgressBar()
        mNotificationService.saveTmpNotification(Notification(message, String(), "message"), audioFile).doOnComplete {
            mView?.hideProgressBar()
            mView?.openFriendsActivity()
        }.subscribe()
    }
}
