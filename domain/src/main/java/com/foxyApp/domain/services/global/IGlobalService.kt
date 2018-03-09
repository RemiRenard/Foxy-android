package com.foxyApp.domain.services.global

import com.foxyApp.data.model.Config
import io.reactivex.Observable

/**
 * Interface of the Global service which contains method called by the presenter.
 */
interface IGlobalService {

    fun getApiConfig(): Observable<Config>

}
