package com.app.foxy.dagger

import com.app.foxy.BaseActivity
import com.app.foxy.FoxyApp
import com.app.foxy.connect.dagger.ConnectComponent
import com.app.foxy.connect.dagger.ConnectModule
import com.app.foxy.friends.dagger.FriendsComponent
import com.app.foxy.friends.dagger.FriendsModule
import com.app.foxy.main.dagger.MainComponent
import com.app.foxy.main.dagger.MainModule
import com.app.foxy.notification.dagger.NotificationComponent
import com.app.foxy.notification.dagger.NotificationModule
import com.app.foxy.profile.dagger.ProfileComponent
import com.app.foxy.profile.dagger.ProfileModule
import com.app.foxy.ranking.dagger.RankingComponent
import com.app.foxy.ranking.dagger.RankingModule
import com.app.foxy.splash.dagger.SplashComponent
import com.app.foxy.splash.dagger.SplashModule
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


