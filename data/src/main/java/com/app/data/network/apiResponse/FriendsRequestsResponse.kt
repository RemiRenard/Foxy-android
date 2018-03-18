package com.app.data.network.apiResponse

import com.app.data.model.User

/**
 * Response from the API when the user wants to get his friends requests.
 */
class FriendsRequestsResponse {

    var requestedBy: User? = null
    var requestId: String? = null
    var notificationId: String? = null
}