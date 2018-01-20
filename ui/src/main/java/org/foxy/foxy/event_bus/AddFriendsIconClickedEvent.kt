package org.foxy.foxy.event_bus

import org.foxy.data.model.User

/**
 * Triggered when the user click on the icon to add a user as a friend
 */
class AddFriendsIconClickedEvent(var user: User)