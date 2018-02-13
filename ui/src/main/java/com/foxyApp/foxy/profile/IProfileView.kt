package com.foxyApp.foxy.profile

import com.foxyApp.data.model.User
import com.foxyApp.foxy.IView

/**
 * Interface of the profile view.
 */
interface IProfileView : IView {

    fun showProfileInformation(user: User)
}
