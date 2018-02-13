package com.foxyApp.foxy.profile.settings

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the settings presenter.
 */
interface ISettingsPresenter : IPresenter<ISettingsView> {

    fun logout()
}
