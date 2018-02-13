package com.foxyApp.domain.services.achievement

import io.reactivex.Observable
import com.foxyApp.data.model.Achievement


/**
 * Interface of the Achievement service which contains method called by the presenter.
 */
interface IAchievementService {

    fun getAchievement(): Observable<List<Achievement>>
}
