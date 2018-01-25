package org.foxy.domain.services.notification

import io.reactivex.Observable
import org.foxy.data.model.Notification
import org.foxy.data.network.api_response.SimpleSuccessResponse
import java.io.File

/**
 * Interface of the User service which contains method called by the presenter.
 */
interface INotificationService {

    fun getTokenOnIoThread(): Observable<String>

    fun sendNotification(userIds: List<String>): Observable<SimpleSuccessResponse>

    fun getNotifications(forceNetworkRefresh: Boolean): Observable<List<Notification>>

    fun saveTmpNotification(notification: Notification, audioFile: File?): Observable<Notification>

    fun markNotificationAsRead(notificationId: String): Observable<SimpleSuccessResponse>

    fun clearNotificationCache()
}
