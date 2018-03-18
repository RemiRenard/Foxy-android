package com.app.foxy.profile.edit

import com.app.data.model.User
import com.app.foxy.IView

/**
 * Interface of the edit profile view.
 */
interface IEditProfileView : IView {

    fun showProfileInformation(user: User)
}
