package org.foxy.foxy.profile.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.profile.IProfilePresenter
import org.foxy.foxy.profile.ProfilePresenter
import org.foxy.foxy.profile.edit.EditProfilePresenter
import org.foxy.foxy.profile.edit.IEditProfilePresenter
import org.foxy.foxy.profile.settings.ISettingsPresenter
import org.foxy.foxy.profile.settings.SettingsPresenter

@Module
class ProfileModule {

    @Provides
    @ProfileScope
    fun provideProfilePresenter(context: Context, userService: IUserService): IProfilePresenter =
            ProfilePresenter(context, userService)

    @Provides
    @ProfileScope
    fun provideSettingsPresenter(userService: IUserService, context: Context): ISettingsPresenter =
            SettingsPresenter(userService, context)

    @Provides
    @ProfileScope
    fun provideEditProfilePresenter(context: Context, userService: IUserService): IEditProfilePresenter =
            EditProfilePresenter(context, userService)
}
