package com.foxyApp.data.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.foxyApp.data.R
import com.foxyApp.data.network.api_error.ResponseError
import retrofit2.HttpException

/**
 * Used to parse the json error response into a custom exception.
 */
class ExceptionHandler {

    companion object {

        /**
         * Return the error message returned by the api
         */
        fun getMessage(throwable: Throwable, context: Context): String {
            var message = context.getString(R.string.Unexpected_server_error)
            if (throwable is HttpException) {
                val body = throwable.response().errorBody()
                try {
                    val responseError = Gson().fromJson(body?.string(), ResponseError::class.java)
                    message = responseError.error?.message
                } catch (e: Exception) {
                    Log.e(ExceptionHandler::class.java.simpleName, "error message: " + e.message)
                }
            } else {
                message = throwable.message
            }
            return message
        }

        /**
         * Return the error code returned by the api
         */
        fun getCode(throwable: Throwable): Int {
            var code = 0
            if (throwable is HttpException) {
                val body = throwable.response().errorBody()
                try {
                    val responseError = Gson().fromJson(body?.string(), ResponseError::class.java)
                    code = responseError.error?.code!!
                } catch (e: Exception) {
                    Log.e(ExceptionHandler::class.java.simpleName, "error message: " + e.message)
                }
            }
            return code
        }
    }
}
