package com.foxyApp.data.database.table

import android.content.ContentValues
import com.foxyApp.data.model.Achievement

/**
 * Table Notification of the local database, which contains mainly SQL
 */
object TableAchievement {

    val DATABASE_TABLE_NAME = "Achievement"
    val TABLE_ACHIEVEMENT_ID = "id"
    val TABLE_ACHIEVEMENT_IS_UNLOCKED = "isUnlocked"

    /**
     * Create the table notification.
     * @return SQL String
     */
    fun createTableAchievement(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_ACHIEVEMENT_ID TEXT NOT NULL PRIMARY KEY," +
                "$TABLE_ACHIEVEMENT_IS_UNLOCKED INTEGER DEFAULT 0)"
    }

    /**
     * @param achievement Achievement
     * @return Content values to create a achievement.
     */
    fun createAchievement(achievement: Achievement): ContentValues {
        val values = ContentValues()
        values.put(TABLE_ACHIEVEMENT_ID, achievement.id)
        if (achievement.isUnlocked) {
            values.put(TABLE_ACHIEVEMENT_IS_UNLOCKED, 1)
        } else {
            values.put(TABLE_ACHIEVEMENT_IS_UNLOCKED, 0)
        }
        return values
    }

    /**
     * Get achievements from database.
     * @return SQL String
     */
    fun getAchievements(): String = "SELECT $TABLE_ACHIEVEMENT_ID, $TABLE_ACHIEVEMENT_IS_UNLOCKED" +
            "FROM $DATABASE_TABLE_NAME"
}