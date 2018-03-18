package com.app.foxy.friends.dagger

import dagger.Subcomponent
import com.app.foxy.friends.FriendsFragment
import com.app.foxy.friends.add.AddFriendsActivity
import com.app.foxy.friends.requests.FriendsRequestsActivity

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
