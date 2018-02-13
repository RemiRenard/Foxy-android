package com.foxyApp.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.foxyApp.data.database.table.*

/**
 * Database open helper class which is extends from SQLiteOpenHelper class.
 */
class DbOpenHelper

/**
 * Constructor
 * @param context Context
 */
(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    /**
     * Method called when the database is created.
     * @param db SQLiteDatabase
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TableFriend.createTableFriend())
        db.execSQL(TableNotification.createTableNotification())
        db.execSQL(TableUser.createTableUser())
        db.execSQL(TableUserRank.createTableUserRank())
        db.execSQL(TableAchievement.createTableAchievement())
    }

    /**
     * Method called when the database is upgrade.
     * @param db         SQLiteDatabase
     * @param oldVersion Integer
     * @param newVersion Integer
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        private val DATABASE_NAME = "database.db"
        private val VERSION = 2
    }
}