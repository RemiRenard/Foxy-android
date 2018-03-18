package com.app.foxy.notification.add

import com.app.foxy.IPresenter
import java.io.File

/**
 * Interface of the Add notification presenter.
 */
interface IAddNotificationPresenter : IPresenter<IAddNotificationView> {

    fun saveTmpNotification(message: String, songId: String, type: String, audioFile: File?)

    fun getSongs(forceNetworkRefresh: Boolean)
}
