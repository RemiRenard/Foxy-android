package com.app.domain.services.global

import android.content.Context
import com.app.data.model.Config
import io.reactivex.Observable

/**
 * Interface of the Global service which contains method called by the presenter.
 */
interface IGlobalService {

    fun getApiConfig(): Observable<Config>

    fun isMainTutorialShowed(context: Context): Boolean

    fun mainTutorialShowed(context: Context)

    fun isSelectSongTutorialShowed(context: Context): Boolean

    fun selectSongTutorialShowed(context: Context)
}
