package com.foxyApp.data.network.api_response

import com.foxyApp.data.model.User

/**
 * Response from the API when the user wants to get his friends requests.
 */
class FriendsRequestsResponse {

    var requestedBy: User? = null
    var requestId: String? = null
    var notificationId: String? = null
}