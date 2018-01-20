package org.foxy.foxy.profile.dagger

import dagger.Subcomponent
import org.foxy.foxy.profile.ProfileFragment
import org.foxy.foxy.profile.edit.EditProfileActivity
import org.foxy.foxy.profile.friends.FriendsActivity
import org.foxy.foxy.profile.friends.add.AddFriendsActivity
import org.foxy.foxy.profile.friends.requests.FriendsRequestsActivity
import org.foxy.foxy.profile.settings.SettingsActivity

/**
 * Profile sub component.
 */
@ProfileScope
@Subcomponent(modules = arrayOf(ProfileModule::class))
interface ProfileComponent {

    // inject target here
    fun inject(target: ProfileFragment)

    fun inject(target: SettingsActivity)

    fun inject(target: FriendsActivity)

    fun inject(target: AddFriendsActivity)

    fun inject(target: FriendsRequestsActivity)

    fun inject(target: EditProfileActivity)
}
