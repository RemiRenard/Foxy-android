package org.foxy.foxy.dagger

import dagger.Component
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.connect.dagger.ConnectComponent
import org.foxy.foxy.connect.dagger.ConnectModule
import org.foxy.foxy.friends.dagger.FriendsComponent
import org.foxy.foxy.friends.dagger.FriendsModule
import org.foxy.foxy.main.dagger.MainComponent
import org.foxy.foxy.main.dagger.MainModule
import org.foxy.foxy.notification.dagger.NotificationComponent
import org.foxy.foxy.notification.dagger.NotificationModule
import org.foxy.foxy.profile.dagger.ProfileComponent
import org.foxy.foxy.profile.dagger.ProfileModule
import org.foxy.foxy.ranking.dagger.RankingComponent
import org.foxy.foxy.ranking.dagger.RankingModule
import org.foxy.foxy.splash.dagger.SplashComponent
import org.foxy.foxy.splash.dagger.SplashModule
import javax.inject.Singleton

/**
 * Dependency injection application component.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, ServiceModule::class))
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


