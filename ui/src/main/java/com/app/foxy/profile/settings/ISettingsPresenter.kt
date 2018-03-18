package com.app.foxy.profile.settings

import com.app.foxy.IPresenter

/**
 * Interface of the settings presenter.
 */
interface ISettingsPresenter : IPresenter<ISettingsView> {

    fun logout()
}
