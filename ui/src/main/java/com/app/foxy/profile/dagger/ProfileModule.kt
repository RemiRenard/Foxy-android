package com.app.foxy.profile.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.app.domain.services.user.IUserService
import com.app.foxy.profile.IProfilePresenter
import com.app.foxy.profile.ProfilePresenter
import com.app.foxy.profile.edit.EditProfilePresenter
import com.app.foxy.profile.edit.IEditProfilePresenter
import com.app.foxy.profile.settings.ISettingsPresenter
import com.app.foxy.profile.settings.SettingsPresenter

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
