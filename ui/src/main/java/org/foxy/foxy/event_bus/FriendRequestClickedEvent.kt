package org.foxy.foxy.event_bus

import org.foxy.data.network.api_response.FriendsRequestsResponse

/**
 * Triggered when the user on accept or decline for a friend request
 */
class FriendRequestClickedEvent(var isAccepted: Boolean, var requestId: String, var notificationId: String,  var request: FriendsRequestsResponse)