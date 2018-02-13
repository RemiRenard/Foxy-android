package com.foxyApp.foxy.event_bus

/**
 * Triggered when the user accept the write storage permission and camera permission
 */
    class CameraPermsResultEvent(var isGranted: Boolean)