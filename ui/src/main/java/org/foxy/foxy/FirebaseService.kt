package org.foxy.foxy

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.foxy.data.Constants
import org.foxy.data.model.Notification
import org.foxy.data.model.User
import org.foxy.foxy.main.MainActivity


class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // Check if message contains a data payload.
        if (remoteMessage?.data?.isNotEmpty()!!) {
            // Create the notification object.

            getNotification(Notification(
                    remoteMessage.data["message"].orEmpty(),
                    Gson().fromJson(remoteMessage.data["user"].orEmpty(), User::class.java), //Gson convert because object received
                    remoteMessage.data["song"].orEmpty()
            ))
        }
    }

    @SuppressLint("NewApi")
    private fun getNotification(notification: Notification) {
        val sound = Uri.parse(notification.song)
        val pendingIntent = PendingIntent.getActivity(this, 1, MainActivity
                .getStartingIntent(this, true), PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create notification for android version greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotifToHighApi(notification, notificationManager, pendingIntent)
        } else {
            sendNotifToLowApi(notification, notificationManager, pendingIntent)
        }
        try {
            val r = RingtoneManager.getRingtone(applicationContext, sound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Notify the user when the api is lower than Oreo
     */
    private fun sendNotifToLowApi(notification: Notification, notificationManager: NotificationManager,
                                  pendingIntent: PendingIntent) {
        val builder = NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.New_Notification, notification.userSource?.username))
                .setContentText(notification.message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_foxy)
                .setPriority(1)
                .setContentIntent(pendingIntent)
        notificationManager.notify(0 /* ID */, builder.build())
    }

    /**
     * Notify the user when the api is higher than Oreo
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotifToHighApi(notification: Notification, notificationManager: NotificationManager,
                                   pendingIntent: PendingIntent) {
        val channel = NotificationChannel(Constants.CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.setSound(null, null)
        notificationManager.createNotificationChannel(channel)
        val builder = android.app.Notification.Builder(this, channel.id)
                .setSmallIcon(R.drawable.logo_foxy)
                .setContentIntent(pendingIntent)
                .setContentTitle(getString(R.string.New_Notification, notification.userSource?.username))
                .setContentText(notification.message)
                .setAutoCancel(true)
        notificationManager.notify(0 /* ID */, builder.build())
    }
}
