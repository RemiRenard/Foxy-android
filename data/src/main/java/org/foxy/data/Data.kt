package org.foxy.data

import android.app.Application
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.foxy.data.cache.Cache
import org.foxy.data.database.DbOpenHelper
import org.foxy.data.network.EchoWebSocketListener
import org.foxy.data.network.NetworkService
import org.foxy.data.network.RetrofitHelper
import timber.log.Timber

/**
 * Singleton which contain the context of the data layer.
 */
object Data {

    var database: BriteDatabase? = null
    var networkService: NetworkService? = null
    var webSocket: WebSocket? = null

    /**
     * Create the database and the network service.
     * @param application Application
     */
    fun init(application: Application) {
        createDatabase(application)
        createNetworkService()
    }

    /**
     * Create the web socket
     */
    fun createWebSocket() {
        val client = OkHttpClient.Builder().build()
        val requestBuilder = Request.Builder().url(Constants.WS_URL_DEV)
        requestBuilder.addHeader(Constants.AUTHORIZATION, Cache.token!!)
        webSocket = client.newWebSocket(requestBuilder.build(), EchoWebSocketListener)
        client.dispatcher().executorService().shutdown()
    }

    /**
     * Close the web socket connection
     */
    fun closeWebSocket() {
        webSocket?.close(1000, "Logout")
    }

    /**
     * Create the network service with the RetrofitHelper class.
     */
    private fun createNetworkService() {
        networkService = RetrofitHelper.createNetworkService(
                RetrofitHelper.createRetrofitBuilder(
                        RetrofitHelper.createHttpClient(
                                RetrofitHelper.createInterceptor()
                        )
                )
        )
    }

    /**
     * Create the database.
     * @param application Application
     */
    private fun createDatabase(application: Application) {
        database = SqlBrite.Builder()
                .logger { message -> Timber.tag("Database").v(message) }
                .build().wrapDatabaseHelper(DbOpenHelper(application), Schedulers.io())
        database?.setLoggingEnabled(true)
    }
}
