package com.foxyApp.data.database.table

import android.content.ContentValues
import com.foxyApp.data.model.UserRank

/**
 * Table Notification of the local database, which contains mainly SQL
 */
object TableUserRank {

    val DATABASE_TABLE_NAME = "Ranking"
    val TABLE_USER_RANK_NAME = "name"
    val TABLE_USER_RANK_AVATAR = "avatar"
    val TABLE_USER_RANK_SCORE = "score"
    val TABLE_USER_RANK_RANK = "rank"
    val TABLE_USER_RANK_TYPE = "type"

    /**
     * Create the table user rank.
     * @return SQL String
     */
    fun createTableUserRank(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_USER_RANK_NAME TEXT NOT NULL," +
                "$TABLE_USER_RANK_AVATAR TEXT," +
                "$TABLE_USER_RANK_SCORE NUMBER," +
                "$TABLE_USER_RANK_RANK NUMBER," +
                "$TABLE_USER_RANK_TYPE TEXT)"
    }

    /**
     * @param user rank UserRank
     * @return Content values to create a user rank.
     */
    fun createUserRank(userRank: UserRank, type: String): ContentValues {
        val values = ContentValues()
        values.put(TABLE_USER_RANK_NAME, userRank.username)
        values.put(TABLE_USER_RANK_AVATAR, userRank.avatar)
        values.put(TABLE_USER_RANK_SCORE, userRank.score)
        values.put(TABLE_USER_RANK_RANK, userRank.rank)
        values.put(TABLE_USER_RANK_TYPE, type)
        return values
    }

    /**
     * Get user ranks from database.
     * @return SQL String
     */
    fun getUserRanks(): String = "SELECT $TABLE_USER_RANK_NAME,$TABLE_USER_RANK_AVATAR," +
            "$TABLE_USER_RANK_SCORE, $TABLE_USER_RANK_RANK, $TABLE_USER_RANK_TYPE " +
            "FROM $DATABASE_TABLE_NAME"
}