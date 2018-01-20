package org.foxy.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.foxy.data.database.table.TableFriend
import org.foxy.data.database.table.TableGame
import org.foxy.data.database.table.TableNotification
import org.foxy.data.database.table.TableUser

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
        db.execSQL(TableGame.createTableGame())
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