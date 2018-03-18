package com.app.foxy.profile

import com.app.data.model.User
import com.app.foxy.IView

/**
 * Interface of the profile view.
 */
interface IProfileView : IView {

    fun showProfileInformation(user: User)
}
