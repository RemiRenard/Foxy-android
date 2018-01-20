package org.foxy.foxy.dagger

import dagger.Component
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.connect.dagger.ConnectComponent
import org.foxy.foxy.connect.dagger.ConnectModule
import org.foxy.foxy.main.dagger.MainComponent
import org.foxy.foxy.main.dagger.MainModule
import org.foxy.foxy.notification.dagger.NotificationComponent
import org.foxy.foxy.notification.dagger.NotificationModule
import org.foxy.foxy.play.findit.dagger.FinditComponent
import org.foxy.foxy.play.findit.dagger.FinditModule
import org.foxy.foxy.profile.dagger.ProfileComponent
import org.foxy.foxy.profile.dagger.ProfileModule
import org.foxy.foxy.search.dagger.SearchComponent
import org.foxy.foxy.search.dagger.SearchModule
import org.foxy.foxy.search.game.dagger.GameComponent
import org.foxy.foxy.search.game.dagger.GameModule
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

    fun plus(searchModule: SearchModule): SearchComponent

    fun plus(gameModule: GameModule): GameComponent

    fun plus(finditModule: FinditModule): FinditComponent

    fun inject(application: FoxyApp)

    fun inject(baseActivity: BaseActivity)
}


