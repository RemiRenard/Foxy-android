package org.foxy.foxy.friends.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.friend.IFriendService
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.adapter.AddFriendsAdapter
import org.foxy.foxy.adapter.FriendsAdapter
import org.foxy.foxy.adapter.FriendsRequestsAdapter
import org.foxy.foxy.friends.FriendsPresenter
import org.foxy.foxy.friends.IFriendsPresenter
import org.foxy.foxy.friends.add.AddFriendsPresenter
import org.foxy.foxy.friends.add.IAddFriendsPresenter
import org.foxy.foxy.friends.requests.FriendsRequestsPresenter
import org.foxy.foxy.friends.requests.IFriendsRequestsPresenter

@Module
class FriendsModule {

    //Presenters

    @Provides
    @FriendsScope
    fun provideFriendsPresenter(context: Context, friendService: IFriendService): IFriendsPresenter =
            FriendsPresenter(friendService, context)

    @Provides
    @FriendsScope
    fun provideAddFriendsPresenter(context: Context, friendService: IFriendService, userService: IUserService): IAddFriendsPresenter =
            AddFriendsPresenter(userService, friendService, context)

    @Provides
    @FriendsScope
    fun provideFriendsRequestsPresenter(context: Context, friendService: IFriendService): IFriendsRequestsPresenter =
            FriendsRequestsPresenter(friendService, context)

    //Adapters

    @Provides
    @FriendsScope
    fun provideFriendsAdapter(context: Context): FriendsAdapter =
            FriendsAdapter(context)

    @Provides
    @FriendsScope
    fun provideAddFriendsAdapter(context: Context): AddFriendsAdapter =
            AddFriendsAdapter(context)

    @Provides
    @FriendsScope
    fun provideFriendsRequestsAdapter(context: Context): FriendsRequestsAdapter =
            FriendsRequestsAdapter(context)
}
