package com.foxyApp.foxy.profile.edit

import com.foxyApp.data.model.User
import com.foxyApp.foxy.IView

/**
 * Interface of the edit profile view.
 */
interface IEditProfileView : IView {

    fun showProfileInformation(user: User)
}
