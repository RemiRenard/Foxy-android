package com.foxyApp.domain.event_bus

import com.foxyApp.data.network.ExceptionHandler

/**
 * Triggered when the session token has expired.
 */
class NetworkErrorEvent(private val throwable: Throwable) {

    var code: Int? = null
        get() = ExceptionHandler.getCode(throwable)
}