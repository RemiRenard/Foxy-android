package com.app.data.network.apiResponse

import com.app.data.model.User

/**
 * Response from the API when the user wants to login.
 */
class ConnectResponse {

    var token: String? = null
    var user: User? = null
}
