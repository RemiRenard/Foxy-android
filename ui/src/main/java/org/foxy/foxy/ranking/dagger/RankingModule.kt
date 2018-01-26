package org.foxy.foxy.profile.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.adapter.RankingAdapter
import org.foxy.foxy.profile.IProfilePresenter
import org.foxy.foxy.profile.ProfilePresenter
import org.foxy.foxy.profile.edit.EditProfilePresenter
import org.foxy.foxy.profile.edit.IEditProfilePresenter
import org.foxy.foxy.profile.settings.ISettingsPresenter
import org.foxy.foxy.profile.settings.SettingsPresenter
import org.foxy.foxy.ranking.IRankingPresenter
import org.foxy.foxy.ranking.RankingPresenter
import org.foxy.foxy.ranking.dagger.RankingScope

@Module
class RankingModule {

    @Provides
    @RankingScope
    fun provideRankingPresenter(context: Context): IRankingPresenter =
            RankingPresenter(context)

    // Adapters
    @Provides
    @RankingScope
    fun provideRankingAdapter(context: Context): RankingAdapter = RankingAdapter(context)
}
