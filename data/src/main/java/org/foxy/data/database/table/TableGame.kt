package org.foxy.data.database.table

import android.content.ContentValues
import org.foxy.data.model.Game

/**
 * Table Game of the local database, which contains mainly SQL
 */
object TableGame {

    val DATABASE_TABLE_NAME = "Game"
    val TABLE_GAME_ID = "id"
    val TABLE_GAME_TITLE = "title"
    val TABLE_GAME_DESCRIPTION = "description"
    val TABLE_GAME_PICTURE = "picture"
    val TABLE_GAME_RULES = "rules"
    val TABLE_GAME_REQUIREMENTS = "requirements"
    val TABLE_GAME_NB_MIN_PLAYER = "nbMinPlayer"
    val TABLE_GAME_NB_MAX_PLAYER = "nbMaxPlayer"

    /**
     * Create the table game.
     * @return SQL String
     */
    fun createTableGame(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_GAME_ID TEXT NOT NULL PRIMARY KEY," +
                "$TABLE_GAME_TITLE TEXT NOT NULL," +
                "$TABLE_GAME_DESCRIPTION TEXT NOT NULL," +
                "$TABLE_GAME_RULES TEXT NOT NULL," +
                "$TABLE_GAME_REQUIREMENTS TEXT NOT NULL," +
                "$TABLE_GAME_NB_MIN_PLAYER INTEGER," +
                "$TABLE_GAME_NB_MAX_PLAYER INTEGER," +
                "$TABLE_GAME_PICTURE TEXT)"
    }

    /**
     * @param game Game?
     * @return Content values to create a game.
     */
    fun createGame(game: Game?): ContentValues {
        val values = ContentValues()
        values.put(TABLE_GAME_ID, game?.id)
        values.put(TABLE_GAME_TITLE, game?.title)
        values.put(TABLE_GAME_DESCRIPTION, game?.description)
        values.put(TABLE_GAME_PICTURE, game?.picture)
        values.put(TABLE_GAME_RULES, game?.rules)
        // Transform array of String in a string
        values.put(TABLE_GAME_REQUIREMENTS, game?.requirements?.joinToString { it } ?: "")
        values.put(TABLE_GAME_NB_MIN_PLAYER, game?.nbMinPlayer)
        values.put(TABLE_GAME_NB_MAX_PLAYER, game?.nbMaxPlayer)
        return values
    }

    /**
     * Get games from database.
     * @return SQL String
     */
    fun getGames(): String = "SELECT * FROM $DATABASE_TABLE_NAME"
}