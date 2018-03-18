package com.app.foxy.profile.edit

import com.app.foxy.IPresenter

/**
 * Interface of the edit profile presenter.
 */
interface IEditProfilePresenter : IPresenter<IEditProfileView> {

    fun getProfile(forceNetworkRefresh: Boolean)
}