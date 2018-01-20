package org.foxy.foxy.profile

import org.foxy.data.model.User
import org.foxy.foxy.IView

/**
 * Interface of the profile view.
 */
interface IProfileView : IView {

    fun showProfileInformation(user: User)
}
