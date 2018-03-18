package com.app.domain.services.notification

import com.app.data.model.Notification
import com.app.data.model.Song
import com.app.data.network.apiResponse.SimpleSuccessResponse
import io.reactivex.Observable
import java.io.File

/**
 * Interface of the User service which contains method called by the presenter.
 */
interface INotificationService {

    fun sendNotification(userIds: List<String>): Observable<SimpleSuccessResponse>

    fun getNotifications(forceNetworkRefresh: Boolean): Observable<List<Notification>>

    fun getSongs(forceNetworkRefresh: Boolean): Observable<List<Song>>

    fun saveTmpNotification(notification: Notification, audioFile: File?): Observable<Notification>

    fun markNotificationAsRead(notificationId: String): Observable<SimpleSuccessResponse>

    fun clearNotificationCache()
}
