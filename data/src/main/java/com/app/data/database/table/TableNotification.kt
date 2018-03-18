package com.app.data.database.table

import android.content.ContentValues
import com.app.data.model.Notification

/**
 * Table Notification of the local database, which contains mainly SQL
 */
object TableNotification {

    val DATABASE_TABLE_NAME = "Notification"
    val TABLE_NOTIFICATION_ID = "id"
    val TABLE_NOTIFICATION_MESSAGE = "message"
    val TABLE_NOTIFICATION_USERNAME = "username"
    val TABLE_NOTIFICATION_CREATED_AT = "created_at"
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
                "$TABLE_NOTIFICATION_MESSAGE TEXT NOT NULL," +
                "$TABLE_NOTIFICATION_USERNAME TEXT," +
                "$TABLE_NOTIFICATION_CREATED_AT TEXT," +
                "$TABLE_NOTIFICATION_TYPE TEXT NOT NULL," +
                "$TABLE_NOTIFICATION_IS_READ INTEGER DEFAULT 0," +
                "$TABLE_NOTIFICATION_SONG TEXT)"
    }

    /**
     * Delete this table.
     */
    fun deleteTable(): String {
        return "DROP TABLE $DATABASE_TABLE_NAME"
    }

    /**
     * @param notification Notification
     * @return Content values to create a notification.
     */
    fun createNotification(notification: Notification): ContentValues {
        val values = ContentValues()
        values.put(TABLE_NOTIFICATION_ID, notification.id)
        values.put(TABLE_NOTIFICATION_MESSAGE, notification.message)
        values.put(TABLE_NOTIFICATION_USERNAME, notification.userSource?.username)
        values.put(TABLE_NOTIFICATION_CREATED_AT, notification.createdAt?.time.toString())
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
    fun getNotifications(): String = "SELECT $TABLE_NOTIFICATION_ID,$TABLE_NOTIFICATION_MESSAGE," +
            "$TABLE_NOTIFICATION_USERNAME, $TABLE_NOTIFICATION_CREATED_AT, $TABLE_NOTIFICATION_TYPE, " +
            "$TABLE_NOTIFICATION_SONG, $TABLE_NOTIFICATION_IS_READ " +
            "FROM $DATABASE_TABLE_NAME"

    fun setNotifToRead(): ContentValues {
        val values = ContentValues()
        values.put(TABLE_NOTIFICATION_IS_READ, 1)
        return values
    }
}