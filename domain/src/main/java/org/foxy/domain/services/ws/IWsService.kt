package org.foxy.domain.services.ws

/**
 * Interface of the web socket service which contains method called by the presenter.
 */
interface IWsService {

    fun connectWebSocket()

    fun disconnectWebSocket()

    fun sendMessage(message: String)
}