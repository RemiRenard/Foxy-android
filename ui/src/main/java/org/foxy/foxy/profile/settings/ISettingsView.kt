package org.foxy.foxy.profile.settings

import org.foxy.foxy.IView

/**
 * Interface of the settings view.
 */
interface ISettingsView : IView {

    fun enableLogoutButton(isEnabled: Boolean)

    fun logoutComplete()
}
