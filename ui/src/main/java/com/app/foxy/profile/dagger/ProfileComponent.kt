package com.app.foxy.profile.dagger

import dagger.Subcomponent
import com.app.foxy.profile.ProfileFragment
import com.app.foxy.profile.edit.EditProfileActivity
import com.app.foxy.profile.settings.SettingsActivity

/**
 * Profile sub component.
 */
@ProfileScope
@Subcomponent(modules = [(ProfileModule::class)])
interface ProfileComponent {

    // inject target here
    fun inject(target: ProfileFragment)

    fun inject(target: SettingsActivity)

    fun inject(target: EditProfileActivity)
}
