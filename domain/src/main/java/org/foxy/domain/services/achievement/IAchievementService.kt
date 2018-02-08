package org.foxy.domain.services.achievement

import io.reactivex.Observable
import org.foxy.data.model.Achievement


/**
 * Interface of the Achievement service which contains method called by the presenter.
 */
interface IAchievementService {

    fun getAchievement(): Observable<List<Achievement>>
}
