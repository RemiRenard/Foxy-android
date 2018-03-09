package com.foxyApp.domain.services.notification

import com.foxyApp.data.model.Notification
import com.foxyApp.data.model.Song
import com.foxyApp.data.network.apiResponse.SimpleSuccessResponse
import io.reactivex.Observable
import java.io.File

/**
 * Interface of the User service which contains method called by the presenter.
 */
interface INotificationService {

    fun getTokenOnIoThread(): Observable<String>

    fun sendNotification(userIds: List<String>): Observable<SimpleSuccessResponse>

    fun getNotifications(forceNetworkRefresh: Boolean): Observable<List<Notification>>

    fun getSongs(forceNetworkRefresh: Boolean): Observable<List<Song>>

    fun saveTmpNotification(notification: Notification, audioFile: File?): Observable<Notification>

    fun markNotificationAsRead(notificationId: String): Observable<SimpleSuccessResponse>

    fun clearNotificationCache()
}
