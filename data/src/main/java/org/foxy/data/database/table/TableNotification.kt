package org.foxy.data.database.table

import android.content.ContentValues
import org.foxy.data.model.Notification

/**
 * Table Notification of the local database, which contains mainly SQL
 */
object TableNotification {

    val DATABASE_TABLE_NAME = "Notification"
    val TABLE_NOTIFICATION_ID = "id"
    val TABLE_NOTIFICATION_TITLE = "title"
    val TABLE_NOTIFICATION_CONTENT = "content"
    val TABLE_NOTIFICATION_TYPE = "type"
    val TABLE_NOTIFICATION_SONG = "song"
    val TABLE_NOTIFICATION_IS_READ = "isRead"

    /**
     * Create the table notification.
     * @return SQL String
     */
    fun createTableNotification(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_NOTIFICATION_ID TEXT NOT NULL PRIMARY KEY," +
                "$TABLE_NOTIFICATION_TITLE TEXT NOT NULL," +
                "$TABLE_NOTIFICATION_CONTENT TEXT NOT NULL," +
                "$TABLE_NOTIFICATION_TYPE TEXT NOT NULL," +
                "$TABLE_NOTIFICATION_IS_READ INTEGER DEFAULT 0," +
                "$TABLE_NOTIFICATION_SONG TEXT)"
    }

    /**
     * @param notification Notification
     * @return Content values to create a notification.
     */
    fun createNotification(notification: Notification): ContentValues {
        val values = ContentValues()
        values.put(TABLE_NOTIFICATION_ID, notification.id)
        values.put(TABLE_NOTIFICATION_TITLE, notification.title)
        values.put(TABLE_NOTIFICATION_CONTENT, notification.content)
        values.put(TABLE_NOTIFICATION_TYPE, notification.type)
        values.put(TABLE_NOTIFICATION_SONG, notification.song)
        if (notification.isRead) {
            values.put(TABLE_NOTIFICATION_IS_READ, 1)
        } else {
            values.put(TABLE_NOTIFICATION_IS_READ, 0)
        }
        return values
    }

    /**
     * Get notifications from database.
     * @return SQL String
     */
    fun getNotifications(): String = "SELECT $TABLE_NOTIFICATION_ID,$TABLE_NOTIFICATION_TITLE," +
            "$TABLE_NOTIFICATION_CONTENT, $TABLE_NOTIFICATION_TYPE, $TABLE_NOTIFICATION_SONG, $TABLE_NOTIFICATION_IS_READ " +
            "FROM $DATABASE_TABLE_NAME"
}