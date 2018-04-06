package com.app.foxy.notification.recordVoice

/**
 * Interface of the "record button" custom view.
 */
interface IButtonRecord {

    fun startRecord()

    fun stopRecord()

    fun requestPermissions()
}
