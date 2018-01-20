package org.foxy.domain.services.game

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.foxy.data.Data
import org.foxy.data.cache.Cache
import org.foxy.data.database.table.TableGame
import org.foxy.data.model.Game
import org.foxy.domain.event_bus.NetworkErrorEvent
import org.greenrobot.eventbus.EventBus

/**
 * Class GameService
 */
class GameService : IGameService {

    private var mGames = ArrayList<Game>()

    /**
     * Get the list of games from the database first and when games from the network are fetched,
     * the list of games is up to date.
     * @return an observable of a list of games.
     */
    override fun getGames(forceNetworkRefresh: Boolean): Observable<List<Game>> {
        mGames.clear()
        return if (Cache.games.isNotEmpty() && !forceNetworkRefresh) {
            Observable.just(Cache.games)
        } else {
            getGamesFromNetwork()
                    .publish { network -> Observable.merge(network, getGamesFromDb().takeUntil(network)) }
                    .doOnNext { Cache.games = it }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * Fetch games from the network.
     * @return an observable of a list of games.
     */
    private fun getGamesFromNetwork(): Observable<List<Game>> {
        return Data.networkService!!
                .getGames(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({ updateGameDb(it) })
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    mGames
                }
    }

    /**
     * Get games from the database.
     * @return an observable of a list of games.
     */
    private fun getGamesFromDb(): Observable<List<Game>> {
        return Data.database!!.createQuery(TableGame.DATABASE_TABLE_NAME, TableGame.getGames())
                .mapToList { cursor ->
                    // Convert a string separate with "," to an arrayList
                    val list: ArrayList<String> = ArrayList()
                    cursor.getString(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_REQUIREMENTS))
                            .split(",").map { list.add(it) }
                    val game = Game()
                    game.id = cursor.getString(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_ID))
                    game.title = cursor.getString(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_TITLE))
                    game.description = cursor.getString(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_DESCRIPTION))
                    game.picture = cursor.getString(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_PICTURE))
                    game.rules = cursor.getString(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_RULES))
                    game.requirements = list
                    game.nbMinPlayer = cursor.getInt(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_NB_MIN_PLAYER))
                    game.nbMaxPlayer = cursor.getInt(cursor.getColumnIndexOrThrow(TableGame.TABLE_GAME_NB_MAX_PLAYER))
                    mGames.add(game)
                    game
                }
    }

    /**
     * Update games in the database.
     * @param games a list of games.
     */
    private fun updateGameDb(games: List<Game>) {
        // Delete all games from the database.
        Data.database?.delete(TableGame.DATABASE_TABLE_NAME, "")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            games.map {
                // Insert a games in the database.
                Data.database?.insert(TableGame.DATABASE_TABLE_NAME, TableGame.createGame(it))
            }
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateGameDb: ", e)
        } finally {
            transaction?.end()
        }
    }
}