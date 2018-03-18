package com.app.domain.eventBus

/**
 * Triggered when the state of internet change.
 */
class NetworkStateChangeEvent(val isInternet: Boolean)
