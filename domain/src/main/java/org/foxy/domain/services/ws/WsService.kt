package org.foxy.domain.services.ws

import org.foxy.data.Data
import org.foxy.data.network.EchoWebSocketListener

/**
 * Class WsService
 */
class WsService : IWsService {

    /**
     * Create the web socket via the data layer
     */
    override fun connectWebSocket() {
        Data.createWebSocket()
    }

    /**
     * Disconnect the web socket via the data layer
     */
    override fun disconnectWebSocket() {
        Data.closeWebSocket()
    }

    /**
     * Send a message to the web socket
     */
    override fun sendMessage(message: String) {
        EchoWebSocketListener.sendMessage(message)
    }
}