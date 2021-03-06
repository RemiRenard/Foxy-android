package com.app.domain.services.notification

import android.util.Log
import com.app.data.Constants
import com.app.data.Data
import com.app.data.cache.Cache
import com.app.data.database.table.TableNotification
import com.app.data.database.table.TableSong
import com.app.data.model.Notification
import com.app.data.model.Song
import com.app.data.model.User
import com.app.data.network.apiRequest.NotificationIdRequest
import com.app.data.network.apiResponse.SimpleSuccessResponse
import com.app.domain.eventBus.NetworkErrorEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*


/**
 * Class NotificationService
 */
class NotificationService : INotificationService {

    private var mNotifications = ArrayList<Notification>()
    private var mSongs = ArrayList<Song>()

    /**
     * Get the list of notification from the database first and when notifications from the network are fetched,
     * the list of notification is up to date.
     * @return an observable of a list of notifications.
     */
    override fun getNotifications(forceNetworkRefresh: Boolean): Observable<List<Notification>> {
        mNotifications.clear()
        return if (Cache.notifications.isNotEmpty() && !forceNetworkRefresh) {
            Observable.just(Cache.notifications)
        } else {
            getNotificationsFromNetwork(Cache.token!!)
                    .publish { network -> Observable.merge(network, getNotificationsFromDb().takeUntil(network)) }
                    .doOnNext { Cache.notifications = it }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * Get users from the database.
     * @return an observable of a list of users.
     */
    private fun getNotificationsFromDb(): Observable<List<Notification>> {
        return Data.database!!.createQuery(TableNotification.DATABASE_TABLE_NAME,
                TableNotification.getNotifications()).mapToList { cursor ->
            val notification = Notification()
            notification.id = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_ID))
            notification.message = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_MESSAGE))
            notification.userSource = User(cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_USERNAME)))
            notification.createdAt = Date(cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_CREATED_AT)).toLong())
            notification.type = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_TYPE))
            notification.song = cursor.getString(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_SONG))
            notification.isRead = cursor.getInt(cursor.getColumnIndexOrThrow(TableNotification.TABLE_NOTIFICATION_IS_READ)) == 1
            mNotifications.add(notification)
            notification
        }
    }

    /**
     * Fetch notification from the network.
     * @return an observable of a list of notifications.
     */
    private fun getNotificationsFromNetwork(token: String): Observable<List<Notification>> {
        return Data.networkService!!
                .getNotifications(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    addNotificationsToDb(it)
                }
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    mNotifications
                }
    }

    /**
     * Update notifications in the database.
     * @param notifications : a list of notifications.
     */
    private fun addNotificationsToDb(notifications: List<Notification>) {
        // Delete all users from the database.
        Data.database?.delete(TableNotification.DATABASE_TABLE_NAME, "")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            for (notification in notifications) {
                // Insert a user in the database.
                Data.database?.insert(TableNotification.DATABASE_TABLE_NAME,
                        TableNotification.createNotification(notification))
            }
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateDb with notification from network: ", e)
        } finally {
            transaction?.end()
        }
    }

    /**
     * Get songs available
     * @param forceNetworkRefresh Boolean
     * @return Observable<List<Song>>
     */
    override fun getSongs(forceNetworkRefresh: Boolean): Observable<List<Song>> {
        mSongs.clear()
        return if (Cache.notifications.isNotEmpty() && !forceNetworkRefresh) {
            Observable.just(Cache.songs)
        } else {
            return getSongsFromNetwork(Cache.token!!)
                    .publish { network -> Observable.merge(network, getSongsFromDb().takeUntil(network)) }
                    .doOnNext { Cache.songs = it }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * Fetch songs from the network.
     * @return an observable of a list of songs.
     */
    private fun getSongsFromNetwork(token: String): Observable<List<Song>> {
        return Data.networkService!!
                .getSongs(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    addSongToDb(it)
                }
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    mSongs
                }
    }

    /**
     * Get users from the database.
     * @return an observable of a list of users.
     */
    private fun getSongsFromDb(): Observable<List<Song>> {
        return Data.database!!.createQuery(TableSong.DATABASE_TABLE_NAME,
                TableSong.getSongs()).mapToList { cursor ->
            val song = Song()
            song.id = cursor.getString(cursor.getColumnIndexOrThrow(TableSong.TABLE_SONG_ID))
            song.url = cursor.getString(cursor.getColumnIndexOrThrow(TableSong.TABLE_SONG_URL))
            song.name = cursor.getString(cursor.getColumnIndexOrThrow(TableSong.TABLE_SONG_NAME))
            song.picture = cursor.getString(cursor.getColumnIndexOrThrow(TableSong.TABLE_SONG_PICTURE))
            mSongs.add(song)
            song
        }
    }

    /**
     * Update songs in the database.
     * @param songs : a list of songs.
     */
    private fun addSongToDb(songs: List<Song>) {
        // Delete all users from the database.
        Data.database?.delete(TableSong.DATABASE_TABLE_NAME, "")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            for (song in songs) {
                // Insert a user in the database.
                Data.database?.insert(TableSong.DATABASE_TABLE_NAME,
                        TableSong.createSong(song))
            }
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateDb with songs from network: ", e)
        } finally {
            transaction?.end()
        }
    }

    /**
     * Send a notification
     * @param userIds List<String>
     * @return Observable<NotifSentResponse>
     */
    override fun sendNotification(userIds: List<String>): Observable<SimpleSuccessResponse> {
        return Data.networkService!!
                .sendNotification(
                        Cache.token!!,
                        RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_TEXT), Cache.tmpNotification?.message!!),
                        RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_TEXT), Cache.tmpNotification?.songId!!),
                        RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_TEXT), Cache.tmpNotification?.type!!),
                        RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_TEXT), userIds.joinToString { it }),
                        // If the file is null or empty
                        if (Cache.audioFile == null || Cache.audioFile!!.length().toInt() == 0) {
                            null
                        } else {
                            RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_AUDIO), Cache.audioFile!!)
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Cache.tmpNotification = Notification()
                    Cache.audioFile = null
                }
    }

    /**
     * Save a temp notification in the cache
     * @param notification Notification
     * @return Observable<Notification>
     */
    override fun saveTmpNotification(notification: Notification, audioFile: File?): Observable<Notification> {
        return Observable.just(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Cache.tmpNotification = it
                    Cache.audioFile = audioFile
                }
    }

    /**
     * Mark a notification as read.
     * @param notificationId String
     * @return Observable<SimpleSuccessResponse>
     */
    override fun markNotificationAsRead(notificationId: String): Observable<SimpleSuccessResponse> {
        try {
            Data.database!!
                    .update(TableNotification.DATABASE_TABLE_NAME,
                            TableNotification.setNotifToRead(),
                            TableNotification.TABLE_NOTIFICATION_ID + "=\'" + notificationId + "\'")
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "marNotificationAsRead: ", e)
        }
        return Data.networkService!!
                .markNotificationAsRead(Cache.token!!, NotificationIdRequest(notificationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * Clear notification & the audio file in cache
     */
    override fun clearNotificationCache() {
        Cache.tmpNotification = null
        Cache.audioFile = null
    }
}
