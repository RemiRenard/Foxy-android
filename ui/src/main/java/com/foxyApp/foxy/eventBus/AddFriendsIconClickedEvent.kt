package com.foxyApp.foxy.eventBus

import com.foxyApp.data.model.User

/**
 * Triggered when the user click on the icon to add a user as a friend
 */
class AddFriendsIconClickedEvent(var user: User)