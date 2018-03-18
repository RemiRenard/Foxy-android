package com.app.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.app.data.database.table.*

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
        createTables(db)
    }

    /**
     * Method called when the database is upgraded.
     * @param db         SQLiteDatabase
     * @param oldVersion Integer
     * @param newVersion Integer
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteTables(db)
        createTables(db)
    }

    /**
     * Create tables of the database.
     */
    private fun createTables(db: SQLiteDatabase) {
        db.execSQL(TableFriend.createTableFriend())
        db.execSQL(TableNotification.createTableNotification())
        db.execSQL(TableUser.createTableUser())
        db.execSQL(TableUserRank.createTableUserRank())
        db.execSQL(TableSong.createTableSong())
        db.execSQL(TableAchievement.createTableAchievement())
    }

    /**
     * Delete tables of the database.
     */
    private fun deleteTables(db: SQLiteDatabase) {
        db.execSQL(TableFriend.deleteTable())
        db.execSQL(TableNotification.deleteTable())
        db.execSQL(TableUser.deleteTable())
        db.execSQL(TableUserRank.deleteTable())
        db.execSQL(TableSong.deleteTable())
        db.execSQL(TableAchievement.deleteTable())
    }

    companion object {

        private val DATABASE_NAME = "database.db"
        private val VERSION = 1
    }
}