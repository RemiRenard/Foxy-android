package com.app.domain.eventBus

import com.app.data.network.ExceptionHandler

/**
 * Triggered when the session token has expired.
 */
class NetworkErrorEvent(private val throwable: Throwable) {

    var code: Int? = null
        get() = ExceptionHandler.getCode(throwable)
}