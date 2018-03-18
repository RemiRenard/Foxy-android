package com.app.foxy.profile.settings

import com.app.foxy.IView

/**
 * Interface of the settings view.
 */
interface ISettingsView : IView {

    fun enableLogoutButton(isEnabled: Boolean)

    fun logoutComplete()
}
