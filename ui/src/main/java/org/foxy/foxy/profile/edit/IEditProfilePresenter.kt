package org.foxy.foxy.profile.edit

import org.foxy.foxy.IPresenter

/**
 * Interface of the edit profile presenter.
 */
interface IEditProfilePresenter : IPresenter<IEditProfileView> {

    fun getProfile(forceNetworkRefresh: Boolean)
}