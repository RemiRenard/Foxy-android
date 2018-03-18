package com.app.foxy.eventBus

import com.app.data.model.User

/**
 * Triggered when the user click on the icon to add a user as a friend
 */
class AddFriendsIconClickedEvent(var user: User)