package com.foxyApp.foxy.dagger

import com.foxyApp.foxy.BaseActivity
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.connect.dagger.ConnectComponent
import com.foxyApp.foxy.connect.dagger.ConnectModule
import com.foxyApp.foxy.friends.dagger.FriendsComponent
import com.foxyApp.foxy.friends.dagger.FriendsModule
import com.foxyApp.foxy.main.dagger.MainComponent
import com.foxyApp.foxy.main.dagger.MainModule
import com.foxyApp.foxy.notification.dagger.NotificationComponent
import com.foxyApp.foxy.notification.dagger.NotificationModule
import com.foxyApp.foxy.profile.dagger.ProfileComponent
import com.foxyApp.foxy.profile.dagger.ProfileModule
import com.foxyApp.foxy.ranking.dagger.RankingComponent
import com.foxyApp.foxy.ranking.dagger.RankingModule
import com.foxyApp.foxy.splash.dagger.SplashComponent
import com.foxyApp.foxy.splash.dagger.SplashModule
import dagger.Component
import javax.inject.Singleton

/**
 * Dependency injection application component.
 */
@Singleton
@Component(modules = [(AppModule::class), (ServiceModule::class)])
interface AppComponent {

    // exposing downstream dependencies
    fun plus(splashModule: SplashModule): SplashComponent

    fun plus(mainModule: MainModule): MainComponent

    fun plus(connectModule: ConnectModule): ConnectComponent

    fun plus(notificationModule: NotificationModule): NotificationComponent

    fun plus(profileModule: ProfileModule): ProfileComponent

    fun plus(friendsModule: FriendsModule): FriendsComponent

    fun plus(rankingModule: RankingModule): RankingComponent

    fun inject(application: FoxyApp)

    fun inject(baseActivity: BaseActivity)
}


