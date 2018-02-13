package com.foxyApp.data.database.table

import android.content.ContentValues
import com.foxyApp.data.model.User

/**
 * Table Friend of the local database, which contains mainly SQL
 */
object TableFriend {

    val DATABASE_TABLE_NAME = "Friend"
    val TABLE_USER_ID = "id"
    val TABLE_USER_EMAIL = "email"
    val TABLE_USER_FIRST_NAME = "firstName"
    val TABLE_USER_LAST_NAME = "lastName"
    val TABLE_USER_USERNAME = "username"
    val TABLE_USER_AVATAR = "avatar"

    /**
     * Create the table friend.
     * @return SQL String
     */
    fun createTableFriend(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_USER_ID TEXT NOT NULL PRIMARY KEY," +
                "$TABLE_USER_EMAIL TEXT NOT NULL," +
                "$TABLE_USER_FIRST_NAME TEXT NOT NULL," +
                "$TABLE_USER_LAST_NAME TEXT NOT NULL," +
                "$TABLE_USER_USERNAME TEXT NOT NULL," +
                "$TABLE_USER_AVATAR TEXT)"
    }

    /**
     * @param user User?
     * @return Content values to create a user.
     */
    fun createFriend(user: User?): ContentValues {
        val values = ContentValues()
        values.put(TABLE_USER_ID, user?.id)
        values.put(TABLE_USER_EMAIL, user?.email)
        values.put(TABLE_USER_FIRST_NAME, user?.firstName)
        values.put(TABLE_USER_LAST_NAME, user?.lastName)
        values.put(TABLE_USER_USERNAME, user?.username)
        values.put(TABLE_USER_AVATAR, user?.avatar)
        return values
    }

    /**
     * Get friends from database.
     * @return SQL String
     */
    fun getFriends(): String = "SELECT * FROM $DATABASE_TABLE_NAME"
}