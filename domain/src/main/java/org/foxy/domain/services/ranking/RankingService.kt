package org.foxy.domain.services.ranking

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.foxy.data.Data
import org.foxy.data.cache.Cache
import org.foxy.data.database.table.TableUserRank
import org.foxy.data.model.UserRank
import org.foxy.data.network.api_response.RankingResponse
import org.foxy.domain.event_bus.NetworkErrorEvent
import org.greenrobot.eventbus.EventBus

/**
 * Class RankingService
 */
class RankingService : IRankingService {
    var mRankingResponse = RankingResponse()

    /**
     * Get the rankings from the database first and when rankings from the network are fetched,
     * the rankings are up to date.
     * @return an observable of a RankingResponse.
     */
    override fun getRanking(): Observable<RankingResponse> {
        Log.i("Service : ", "getRanking called")
        mRankingResponse = RankingResponse()
        return getRankingsFromNetwork()
                .publish { network -> Observable.merge(network, getRankingsFromDb().takeUntil(network)) }
                .doOnNext { Cache.rankings = it}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * Get users from the database.
     * @return an observable of a list of users.
     */
    private fun getRankingsFromDb(): Observable<RankingResponse> {
        Log.i("Service : ", "getRankingsFromDb called")
        mRankingResponse = RankingResponse()
        Data.database!!.createQuery(TableUserRank.DATABASE_TABLE_NAME,
                TableUserRank.getUserRanks()).mapToList { cursor ->
            val userRank = UserRank()
            userRank.username = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_NAME))
            userRank.avatar = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_AVATAR))
            userRank.score = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_SCORE))
            userRank.rank = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_RANK))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_TYPE))
            Log.i("getUserRanks:", userRank.username + " as " + type)
            when (type) {
                "current" -> mRankingResponse.currentUserData = userRank
                "global" -> mRankingResponse.globalRanking.add(userRank)
                "weekly" -> mRankingResponse.weeklyRanking.add(userRank)
                "daily" -> mRankingResponse.dailyRanking.add(userRank)
            }
            userRank
        }.subscribe()
        return Observable.just(mRankingResponse)
    }


    /**
     * Fetch rankings from the network.
     * @return an observable of a RankingResponse.
     */
    private fun getRankingsFromNetwork(): Observable<RankingResponse> {
        Log.i("Service : ", "getRankingsFromNetwork called")
        return Data.networkService!!
                .getRanking(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({
                    addRankingsToDb(it) })
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    mRankingResponse
                }
    }

    /**
     * Update rankings in the database.
     * @param rankings : RankingResponse.
     */
    private fun addRankingsToDb(rankings: RankingResponse) {
        Log.i("Service : ", "addRankingsToDb called")
        // Delete all user ranks from the database.
        Data.database?.delete(TableUserRank.DATABASE_TABLE_NAME, "")
        Log.i("addRankingsToDb","deleteDb" )
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            // Insert a current user rank in the database.
            Log.i("addRankingsToDb","insertCurrent" )
            Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                    TableUserRank.createUserRank(rankings.currentUserData!!, "current"))
            Log.i("addRankingsToDb","insertGlobal" )
            for (gRanking in rankings.globalRanking) {
                // Insert a user rank global in the database.
                Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                        TableUserRank.createUserRank(gRanking, "global"))
            }
            Log.i("addRankingsToDb","insertWeekly" )
            for (wRanking in rankings.weeklyRanking) {
                // Insert a user rank weekly in the database.
                Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                        TableUserRank.createUserRank(wRanking, "weekly"))
            }
            Log.i("addRankingsToDb","insertDaily" )
            for (dRanking in rankings.dailyRanking) {
                // Insert a user rank daily in the database.
                Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                        TableUserRank.createUserRank(dRanking, "daily"))
            }
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateDb with user rank from network: ", e)
        } finally {
            transaction?.end()
        }
    }
}
