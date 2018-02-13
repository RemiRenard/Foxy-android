package com.foxyApp.foxy.profile.settings

import com.foxyApp.foxy.IView

/**
 * Interface of the settings view.
 */
interface ISettingsView : IView {

    fun enableLogoutButton(isEnabled: Boolean)

    fun logoutComplete()
}
