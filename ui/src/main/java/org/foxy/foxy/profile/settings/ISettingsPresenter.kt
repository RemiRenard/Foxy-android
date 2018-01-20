package org.foxy.foxy.profile.settings

import org.foxy.foxy.IPresenter

/**
 * Interface of the settings presenter.
 */
interface ISettingsPresenter : IPresenter<ISettingsView> {

    fun logout()
}
