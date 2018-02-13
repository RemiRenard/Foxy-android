package com.foxyApp.domain.services.ranking

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.foxyApp.data.Data
import com.foxyApp.data.cache.Cache
import com.foxyApp.data.database.table.TableUserRank
import com.foxyApp.data.model.UserRank
import com.foxyApp.data.network.api_response.RankingResponse
import com.foxyApp.domain.event_bus.NetworkErrorEvent
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
        mRankingResponse = RankingResponse()
        return getRankingsFromNetwork()
                .publish { network -> Observable.merge(network, getRankingsFromDb().takeUntil(network)) }
                .doOnNext { Cache.rankings = it }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * Get rankings from the database.
     * @return an observable of a list of users.
     */
    private fun getRankingsFromDb(): Observable<RankingResponse> {
        mRankingResponse = RankingResponse()
        Data.database!!.createQuery(TableUserRank.DATABASE_TABLE_NAME,
                TableUserRank.getUserRanks()).mapToList { cursor ->
            val userRank = UserRank()
            userRank.username = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_NAME))
            userRank.avatar = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_AVATAR))
            userRank.score = cursor.getInt(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_SCORE))
            userRank.rank = cursor.getInt(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_RANK))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(TableUserRank.TABLE_USER_RANK_TYPE))
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
        return Data.networkService!!
                .getRanking(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({
                    addRankingsToDb(it)
                })
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
        // Delete all user ranks from the database.
        Data.database?.delete(TableUserRank.DATABASE_TABLE_NAME, "")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            // Insert a current user rank in the database.
            Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                    TableUserRank.createUserRank(rankings.currentUserData!!, "current"))
            for (gRanking in rankings.globalRanking) {
                // Insert a user rank global in the database.
                Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                        TableUserRank.createUserRank(gRanking, "global"))
            }
            for (wRanking in rankings.weeklyRanking) {
                // Insert a user rank weekly in the database.
                Data.database?.insert(TableUserRank.DATABASE_TABLE_NAME,
                        TableUserRank.createUserRank(wRanking, "weekly"))
            }
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
