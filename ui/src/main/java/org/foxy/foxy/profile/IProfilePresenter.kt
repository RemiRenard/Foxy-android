package org.foxy.foxy.profile

import org.foxy.foxy.IPresenter
import java.io.File

/**
 * Interface of the profile presenter.
 */
interface IProfilePresenter : IPresenter<IProfileView> {

    fun setUpPhotoFile(): File?

    fun getProfile(forceNetworkRefresh: Boolean)

    fun updateProfilePicture(picture: File)
}