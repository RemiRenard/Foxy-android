package org.foxy.foxy.event_bus


import org.foxy.data.model.User

/**
 * Triggered when the list of friends selected to send notification is updated.
 */
class FriendsSelectedNotifEvent(val friendsSelected: List<User>)
