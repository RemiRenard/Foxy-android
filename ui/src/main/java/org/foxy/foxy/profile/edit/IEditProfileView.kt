package org.foxy.foxy.profile.edit

import org.foxy.data.model.User
import org.foxy.foxy.IView

/**
 * Interface of the edit profile view.
 */
interface IEditProfileView : IView {

    fun showProfileInformation(user: User)
}
