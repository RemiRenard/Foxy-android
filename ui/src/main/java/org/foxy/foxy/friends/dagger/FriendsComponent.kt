package org.foxy.foxy.friends.dagger

import dagger.Subcomponent
import org.foxy.foxy.friends.FriendsFragment
import org.foxy.foxy.friends.add.AddFriendsActivity
import org.foxy.foxy.friends.requests.FriendsRequestsActivity

/**
 * Friends sub component.
 */
@FriendsScope
@Subcomponent(modules = arrayOf(FriendsModule::class))
interface FriendsComponent {

    // inject target here
    fun inject(target: FriendsFragment)

    fun inject(target: AddFriendsActivity)

    fun inject(target: FriendsRequestsActivity)
}
