package com.foxyApp.foxy.event_bus


import com.foxyApp.data.model.User

/**
 * Triggered when the list of friends selected to send notification is updated.
 */
class FriendsSelectedNotifEvent(val friendsSelected: List<User>)
