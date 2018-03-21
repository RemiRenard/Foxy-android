package com.app.domain.services.global

import android.content.Context
import com.app.data.Constants
import com.app.data.Data
import com.app.data.model.Config
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Class GlobalService
 */
class GlobalService : IGlobalService {

    override fun getApiConfig(): Observable<Config> {
        return Data.networkService!!
                .getConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun isMainTutorialShowed(context: Context): Boolean {
        return context.getSharedPreferences(Constants.TUTORIAL_MAIN_ALREADY_PLAYED, Context.MODE_PRIVATE)
                .getBoolean(Constants.TUTORIAL_MAIN_ALREADY_PLAYED, false)
    }

    override fun mainTutorialShowed(context: Context) {
        val editor = context.getSharedPreferences(Constants.TUTORIAL_MAIN_ALREADY_PLAYED, Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(Constants.TUTORIAL_MAIN_ALREADY_PLAYED, true)?.apply()
    }

    override fun isSelectSongTutorialShowed(context: Context): Boolean {
        return context.getSharedPreferences(Constants.TUTORIAL_SELECT_SONG_ALREADY_PLAYED, Context.MODE_PRIVATE)
                .getBoolean(Constants.TUTORIAL_SELECT_SONG_ALREADY_PLAYED, false)
    }

    override fun selectSongTutorialShowed(context: Context) {
        val editor = context.getSharedPreferences(Constants.TUTORIAL_SELECT_SONG_ALREADY_PLAYED, Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(Constants.TUTORIAL_SELECT_SONG_ALREADY_PLAYED, true)?.apply()
    }

    override fun isRankingTutorialShowed(context: Context): Boolean {
        return context.getSharedPreferences(Constants.TUTORIAL_SELECT_SONG_ALREADY_PLAYED, Context.MODE_PRIVATE)
                .getBoolean(Constants.TUTORIAL_SELECT_SONG_ALREADY_PLAYED, false)
    }

    override fun rankingTutorialShowed(context: Context) {
        val editor = context.getSharedPreferences(Constants.TUTORIAL_RANKING_ALREADY_PLAYED, Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(Constants.TUTORIAL_RANKING_ALREADY_PLAYED, true)?.apply()
    }

}
