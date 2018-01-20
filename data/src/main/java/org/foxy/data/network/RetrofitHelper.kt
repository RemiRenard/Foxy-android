package org.foxy.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.foxy.data.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    /**
     * Create the interceptor of retrofit.
     * @return Interceptor
     */
    fun createInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                    .addHeader(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
                    .addHeader(Constants.API_KEY_TITLE, Constants.API_KEY_VALUE)
                    .build()
            chain.proceed(newRequest)
        }
    }

    /**
     * Create the client OkHttp for retrofit.
     * @param interceptor Interceptor
     * @return OkHttpClient
     */
    fun createHttpClient(interceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(interceptor)
        builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }

    /**
     * Create the retrofit builder.
     * @param client OkHttpClient
     * @return Retrofit
     */
    fun createRetrofitBuilder(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    /**
     * Return the network service of the app.
     * @param retrofit Retrofit
     * @return NetworkService
     */
    fun createNetworkService(retrofit: Retrofit): NetworkService =
            retrofit.create(NetworkService::class.java)
}
