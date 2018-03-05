package com.foxyApp.foxy.friends.dagger

import dagger.Subcomponent
import com.foxyApp.foxy.friends.FriendsFragment
import com.foxyApp.foxy.friends.add.AddFriendsActivity
import com.foxyApp.foxy.friends.requests.FriendsRequestsActivity

/**
 * Friends sub component.
 */
@FriendsScope
@Subcomponent(modules = [(FriendsModule::class)])
interface FriendsComponent {

    // inject target here
    fun inject(target: FriendsFragment)

    fun inject(target: AddFriendsActivity)

    fun inject(target: FriendsRequestsActivity)
}
