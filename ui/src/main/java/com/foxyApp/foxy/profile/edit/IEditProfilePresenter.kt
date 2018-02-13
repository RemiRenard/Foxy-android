package com.foxyApp.foxy.profile.edit

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the edit profile presenter.
 */
interface IEditProfilePresenter : IPresenter<IEditProfileView> {

    fun getProfile(forceNetworkRefresh: Boolean)
}