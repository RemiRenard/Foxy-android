package com.app.foxy.eventBus


import com.app.data.model.Song

/**
 * Triggered when the user has choose a notif.
 */
class SongSelectedNotifEvent(val song: Song)
