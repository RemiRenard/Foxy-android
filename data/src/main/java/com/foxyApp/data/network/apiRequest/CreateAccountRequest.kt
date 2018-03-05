package com.foxyApp.data.network.apiRequest

import java.util.*

class CreateAccountRequest(private var email: String, private var password: String, private var firstName: String,
                           private var lastName: String, private var username: String, private var birthday: Date,
                           private var deviceId: String)