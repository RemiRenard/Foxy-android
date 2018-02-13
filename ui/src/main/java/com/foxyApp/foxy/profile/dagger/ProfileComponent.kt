package com.foxyApp.foxy.profile.dagger

import dagger.Subcomponent
import com.foxyApp.foxy.profile.ProfileFragment
import com.foxyApp.foxy.profile.edit.EditProfileActivity
import com.foxyApp.foxy.profile.settings.SettingsActivity

/**
 * Profile sub component.
 */
@ProfileScope
@Subcomponent(modules = arrayOf(ProfileModule::class))
interface ProfileComponent {

    // inject target here
    fun inject(target: ProfileFragment)

    fun inject(target: SettingsActivity)

    fun inject(target: EditProfileActivity)
}
