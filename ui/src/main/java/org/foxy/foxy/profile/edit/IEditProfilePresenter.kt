package org.foxy.foxy.profile.edit

import org.foxy.foxy.IPresenter
import java.io.File

/**
 * Interface of the edit profile presenter.
 */
interface IEditProfilePresenter : IPresenter<IEditProfileView> {

    fun setUpPhotoFile(): File?

    fun getProfile(forceNetworkRefresh: Boolean)

    fun updateProfilePicture(picture: File)
}