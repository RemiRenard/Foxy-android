package org.foxy.foxy.profile.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.friend.IFriendService
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.adapter.AddFriendsAdapter
import org.foxy.foxy.adapter.FriendsAdapter
import org.foxy.foxy.adapter.FriendsRequestsAdapter
import org.foxy.foxy.profile.IProfilePresenter
import org.foxy.foxy.profile.ProfilePresenter
import org.foxy.foxy.profile.edit.EditProfilePresenter
import org.foxy.foxy.profile.edit.IEditProfilePresenter
import org.foxy.foxy.profile.friends.FriendsPresenter
import org.foxy.foxy.profile.friends.IFriendsPresenter
import org.foxy.foxy.profile.friends.add.AddFriendsPresenter
import org.foxy.foxy.profile.friends.add.IAddFriendsPresenter
import org.foxy.foxy.profile.friends.requests.FriendsRequestsPresenter
import org.foxy.foxy.profile.friends.requests.IFriendsRequestsPresenter
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
    fun provideFriendsPresenter(friendService: IFriendService, context: Context): IFriendsPresenter =
            FriendsPresenter(friendService, context)

    @Provides
    @ProfileScope
    fun provideAddFriendsPresenter(userService: IUserService, friendService: IFriendService,
                                   context: Context): IAddFriendsPresenter =
            AddFriendsPresenter(userService, friendService, context)

    @Provides
    @ProfileScope
    fun provideFriendsRequestedPresenter(friendService: IFriendService,
                                         context: Context): IFriendsRequestsPresenter =
            FriendsRequestsPresenter(friendService, context)

    @Provides
    @ProfileScope
    fun provideEditProfilePresenter(context: Context, userService: IUserService): IEditProfilePresenter =
            EditProfilePresenter(context, userService)

    // Adapters
    @Provides
    @ProfileScope
    fun provideFriendsAdapter(context: Context): FriendsAdapter = FriendsAdapter(context)

    @Provides
    @ProfileScope
    fun provideAddFriendsAdapter(context: Context): AddFriendsAdapter = AddFriendsAdapter(context)

    @Provides
    @ProfileScope
    fun provideFriendsRequestsAdapter(context: Context): FriendsRequestsAdapter = FriendsRequestsAdapter(context)
}
