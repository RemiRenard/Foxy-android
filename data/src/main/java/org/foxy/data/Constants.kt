package org.foxy.data

/**
 * Class which contains constants value.
 */
object Constants {

    const val CONTENT_TYPE = "Content-Type"
    const val APPLICATION_JSON = "application/json"
    const val API_KEY_TITLE = "X-API-Key"
    const val API_KEY_VALUE = "dev"
    const val MEDIA_TYPE_IMAGE = "image/jpeg"
    const val MEDIA_TYPE_AUDIO = "audio/mp3"
    const val MEDIA_TYPE_TEXT = "text/plain"
    const val APP_TOKEN_PREF = "appTokenPref"
    const val TOKEN = "token"
    const val PROJECT_NUMBER = "780116430355"
    const val GCM = "GCM"
    const val AUTHORIZATION = "Authorization"
    const val EXTRA_IS_NEW_NOTIFICATION = "EXTRA_IS_NEW_NOTIFICATION"
    const val BUNDLE_IS_NEW_NOTIFICATION = "BUNDLE_IS_NEW_NOTIFICATION"
    const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
    const val EXTRA_GAME = "EXTRA_GAME"
    const val CHANNEL_ID = "CHANNEL_ID"
    const val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME"
    const val REQUEST_PERMISSION_WRITE_STORAGE = 1
    const val REQUEST_PERMISSION_GROUP_CAMERA = 5
    const val TYPE_LEFT = 240
    const val TYPE_RIGHT = 241

    // DEV
    const val SERVER_URL_DEV = "http://192.168.1.135:3000/api/"
    const val WS_URL_DEV = "ws://foxy-server.herokuapp.com:8889"

    //PROD
    const val SERVER_URL_PROD = "http://foxy-server.herokuapp.com/api/"
    const val WS_URL_PROD = "ws://foxy-server.herokuapp.com:8889"

    //NETWORK ERROR CODE
    const val SESSION_EXPIRED = 104
}
