package org.foxy.foxy.ranking.dagger

import android.content.Context
import dagger.Provides
import dagger.Subcomponent
import org.foxy.foxy.adapter.NotificationAdapter
import org.foxy.foxy.adapter.RankingAdapter
import org.foxy.foxy.notification.dagger.NotificationScope
import org.foxy.foxy.profile.dagger.RankingModule
import org.foxy.foxy.ranking.RankingFragment

/**
 * Ranking sub component.
 */
@RankingScope
@Subcomponent(modules = arrayOf(RankingModule::class))
interface RankingComponent {

    // inject target here
    fun inject(target: RankingFragment)

}
