package org.foxy.data.network.api_response

import org.foxy.data.model.User

/**
 * Response from the API when the user wants to login.
 */
class ConnectResponse {

    var token: String? = null
    var user: User? = null
}
