package com.foxyApp.foxy.profile.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.profile.IProfilePresenter
import com.foxyApp.foxy.profile.ProfilePresenter
import com.foxyApp.foxy.profile.edit.EditProfilePresenter
import com.foxyApp.foxy.profile.edit.IEditProfilePresenter
import com.foxyApp.foxy.profile.settings.ISettingsPresenter
import com.foxyApp.foxy.profile.settings.SettingsPresenter

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
