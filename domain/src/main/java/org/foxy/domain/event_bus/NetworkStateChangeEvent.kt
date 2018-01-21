package org.foxy.domain.event_bus

/**
 * Triggered when the state of internet change.
 */
class NetworkStateChangeEvent(val isInternet: Boolean)