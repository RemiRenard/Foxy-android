package org.foxy.foxy.notification.details

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.foxy.foxy.notification.dagger.NotificationScope

/**
 * Notification presenter
 */
@NotificationScope
class DetailsNotificationPresenter(private val mContext: Context) : IDetailsNotificationPresenter {

    private var mView: IDetailsNotificationView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IDetailsNotificationView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }
}
