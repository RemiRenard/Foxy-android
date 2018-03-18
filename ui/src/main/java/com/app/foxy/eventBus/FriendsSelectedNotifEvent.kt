package com.app.foxy.eventBus


import com.app.data.model.User

/**
 * Triggered when the list of friends selected to send notification is updated.
 */
class FriendsSelectedNotifEvent(val friendsSelected: List<User>)
