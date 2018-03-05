package com.foxyApp.domain.services.achievement

import com.foxyApp.data.model.Achievement
import io.reactivex.Observable


/**
 * Interface of the Achievement service which contains method called by the presenter.
 */
interface IAchievementService {

    fun getAchievement(): Observable<List<Achievement>>
}
