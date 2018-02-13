package com.foxyApp.domain

import android.app.Application
import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.foxyApp.data.Data
import com.foxyApp.domain.event_bus.NetworkStateChangeEvent
import org.greenrobot.eventbus.EventBus

/**
 * Singleton which contains methods of the domain layer which require a context.
 */
object Domain {

    /**
     * Initialize the Domain layer.
     */
    fun init(application: Application) {
        Data.init(application)
        initNetworkState(application)
    }

    /**
     * Initialize an observable which send an event NetworkStateChangeEvent()
     */
    private fun initNetworkState(context: Context) {
        ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { connectivity -> EventBus.getDefault().post(NetworkStateChangeEvent(connectivity.isAvailable)) }
    }
}
