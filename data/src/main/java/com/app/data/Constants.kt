package com.app.data

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
    const val CHANNEL_ID = "CHANNEL_ID"
    const val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME"
    const val REQUEST_PERMISSION_WRITE_STORAGE = 1
    const val REQUEST_PERMISSION_GROUP_CAMERA = 5
    const val DEFAULT_SONG_LOCATION = "https://s3.eu-west-3.amazonaws.com/foxy-sounds/default_song.mp3"
    const val RECORD_MAX_DURATION = 5000 //milliseconds
    const val TUTORIAL_MAIN_ALREADY_PLAYED = "TUTORIAL_MAIN_ALREADY_PLAYED"
    const val TUTORIAL_SELECT_SONG_ALREADY_PLAYED = "TUTORIAL_SELECT_SONG_ALREADY_PLAYED"
    const val TUTORIAL_RANKING_ALREADY_PLAYED = "TUTORIAL_RANKING_ALREADY_PLAYED"

    // DEV
    const val SERVER_URL_DEV = "http://192.168.1.138:8888/api/"

    //PROD
    const val SERVER_URL_PROD = "http://foxy-api.herokuapp.com/api/"

    //NETWORK ERROR CODE
    const val SESSION_EXPIRED = 104
}
