package com.foxyApp.domain.services.global

import com.foxyApp.data.Data
import com.foxyApp.data.model.Config
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
}
