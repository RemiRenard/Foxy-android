package org.foxy.data.network

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.greenrobot.eventbus.EventBus

/**
 * Web socket listener
 */
object EchoWebSocketListener : WebSocketListener() {

    private var mWebSocket: WebSocket? = null

    override fun onOpen(webSocket: WebSocket, response: Response) {
        mWebSocket = webSocket
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        EventBus.getDefault().post(MessageReceived(text))
    }

    fun sendMessage(text: String) {
        mWebSocket?.send(text)
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        t?.printStackTrace()
    }

    class MessageReceived(var text: String)
}