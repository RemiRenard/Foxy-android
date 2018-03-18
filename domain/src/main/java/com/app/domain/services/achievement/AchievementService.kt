package com.app.domain.services.achievement

import android.util.Log
import com.app.data.Data
import com.app.data.cache.Cache
import com.app.data.database.table.TableAchievement
import com.app.data.model.Achievement
import com.app.domain.eventBus.NetworkErrorEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Class AchievementService
 */
class AchievementService : IAchievementService {
    var mAchievements = ArrayList<Achievement>()

    /**
     * Get the achievements from the database first and when achievements from the network are fetched,
     * the achievements are up to date.
     * @return an observable of a AchievementResponse.
     */
    override fun getAchievement(): Observable<List<Achievement>> {
        mAchievements.clear()
        return getAchievementsFromNetwork()
                .publish { network -> Observable.merge(network, getAchievementsFromDb().takeUntil(network)) }
                .doOnNext { Cache.achievements = it }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * Get achievements from the database.
     * @return an observable of a list of users.
     */
    private fun getAchievementsFromDb(): Observable<List<Achievement>> {
        return Data.database!!.createQuery(TableAchievement.DATABASE_TABLE_NAME,
                TableAchievement.getAchievements()).mapToList { cursor ->
            val achievement = Achievement()
            achievement.id = cursor.getString(cursor.getColumnIndexOrThrow(TableAchievement.TABLE_ACHIEVEMENT_ID))
            achievement.isUnlocked = cursor.getInt(cursor.getColumnIndexOrThrow(TableAchievement.TABLE_ACHIEVEMENT_IS_UNLOCKED)) == 1
            mAchievements.add(achievement)
            achievement
        }
    }


    /**
     * Fetch achievements from the network.
     * @return an observable of a AchievementResponse.
     */
    private fun getAchievementsFromNetwork(): Observable<List<Achievement>> {
        return Data.networkService!!
                .getAchievements(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({
                    addAchievementsToDb(it)
                })
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    mAchievements
                }
    }

    /**
     * Update achievements in the database.
     * @param achievements : AchievementResponse.
     */
    private fun addAchievementsToDb(achievements: List<Achievement>) {
        // Delete all achievements from the database.
        Data.database?.delete(TableAchievement.DATABASE_TABLE_NAME, "")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            for (achievement in achievements) {
                // Insert a achievement in the database.
                Data.database?.insert(TableAchievement.DATABASE_TABLE_NAME,
                        TableAchievement.createAchievement(achievement))
            }
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateDb with achievement from network: ", e)
        } finally {
            transaction?.end()
        }
    }
}
