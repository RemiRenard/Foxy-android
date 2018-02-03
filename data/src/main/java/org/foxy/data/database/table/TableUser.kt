package org.foxy.data.database.table

import android.content.ContentValues
import com.google.gson.Gson
import org.foxy.data.Data
import org.foxy.data.model.User

/**
 * Table User of the local database, which contains mainly SQL
 */
object TableUser {

    val DATABASE_TABLE_NAME = "User"
    val TABLE_USER_ID = "id"
    val TABLE_USER_EMAIL = "email"
    val TABLE_USER_FIRST_NAME = "firstName"
    val TABLE_USER_LAST_NAME = "lastName"
    val TABLE_USER_USERNAME = "username"
    val TABLE_USER_BIRTHDAY = "birthday"
    val TABLE_USER_EMAIL_VERIFIED = "emailVerified"
    val TABLE_USER_AVATAR = "avatar"
    val TABLE_USER_STATS = "stats"
    private val TABLE_USER_IS_CURRENT = "isCurrent"

    /**
     * Create the table user.
     * @return SQL String
     */
    fun createTableUser(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_USER_ID TEXT NOT NULL PRIMARY KEY," +
                "$TABLE_USER_EMAIL TEXT NOT NULL," +
                "$TABLE_USER_FIRST_NAME TEXT NOT NULL," +
                "$TABLE_USER_LAST_NAME TEXT NOT NULL," +
                "$TABLE_USER_USERNAME TEXT NOT NULL," +
                "$TABLE_USER_STATS TEXT," +
                "$TABLE_USER_IS_CURRENT INTEGER DEFAULT 0," +
                "$TABLE_USER_EMAIL_VERIFIED INTEGER DEFAULT 0," +
                "$TABLE_USER_AVATAR TEXT," +
                "$TABLE_USER_BIRTHDAY LONG NOT NULL)"
    }

    /**
     * @param user User?
     * @return Content values to create a user.
     */
    fun insertUser(user: User?, isCurrent: Boolean): ContentValues {
        // Delete the current user from the database.
        Data.database?.delete(TableUser.DATABASE_TABLE_NAME, "isCurrent = 1")
        val values = ContentValues()
        values.put(TABLE_USER_ID, user?.id)
        values.put(TABLE_USER_EMAIL, user?.email)
        values.put(TABLE_USER_FIRST_NAME, user?.firstName)
        values.put(TABLE_USER_LAST_NAME, user?.lastName)
        values.put(TABLE_USER_USERNAME, user?.username)
        values.put(TABLE_USER_BIRTHDAY, user?.birthday?.time)
        values.put(TABLE_USER_EMAIL_VERIFIED, user?.emailVerified)
        values.put(TABLE_USER_AVATAR, user?.avatar)
        values.put(TABLE_USER_STATS, Gson().toJson(user?.stats))
        if (isCurrent) {
            values.put(TABLE_USER_IS_CURRENT, 1)
        }
        return values
    }

    /**
     * Get users from database.
     * @return SQL String
     */
    fun getCurrentUser(): String = "SELECT * FROM $DATABASE_TABLE_NAME WHERE isCurrent = 1 LIMIT 1"
}