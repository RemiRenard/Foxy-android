package org.foxy.domain.event_bus

import org.foxy.data.network.ExceptionHandler

/**
 * Triggered when the session token has expired.
 */
class NetworkErrorEvent(private val throwable: Throwable) {

    var code: Int? = null
        get() = ExceptionHandler.getCode(throwable)
}