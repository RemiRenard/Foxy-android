package com.foxyApp.foxy.event_bus

import com.foxyApp.data.network.api_response.FriendsRequestsResponse

/**
 * Triggered when the user on accept or decline for a friend request
 */
class FriendRequestClickedEvent(var isAccepted: Boolean, var requestId: String, var notificationId: String,  var request: FriendsRequestsResponse)