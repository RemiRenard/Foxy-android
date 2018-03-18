package com.app.foxy.friends.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.app.domain.services.friend.IFriendService
import com.app.domain.services.user.IUserService
import com.app.foxy.adapter.AddFriendsAdapter
import com.app.foxy.adapter.FriendsAdapter
import com.app.foxy.adapter.FriendsRequestsAdapter
import com.app.foxy.friends.FriendsPresenter
import com.app.foxy.friends.IFriendsPresenter
import com.app.foxy.friends.add.AddFriendsPresenter
import com.app.foxy.friends.add.IAddFriendsPresenter
import com.app.foxy.friends.requests.FriendsRequestsPresenter
import com.app.foxy.friends.requests.IFriendsRequestsPresenter

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
