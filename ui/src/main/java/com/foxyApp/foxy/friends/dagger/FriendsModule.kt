package com.foxyApp.foxy.friends.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.friend.IFriendService
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.adapter.AddFriendsAdapter
import com.foxyApp.foxy.adapter.FriendsAdapter
import com.foxyApp.foxy.adapter.FriendsRequestsAdapter
import com.foxyApp.foxy.friends.FriendsPresenter
import com.foxyApp.foxy.friends.IFriendsPresenter
import com.foxyApp.foxy.friends.add.AddFriendsPresenter
import com.foxyApp.foxy.friends.add.IAddFriendsPresenter
import com.foxyApp.foxy.friends.requests.FriendsRequestsPresenter
import com.foxyApp.foxy.friends.requests.IFriendsRequestsPresenter

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
