package com.foxyApp.foxy.splash

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.connect.ConnectActivity
import com.foxyApp.foxy.custom.CustomCircle
import com.foxyApp.foxy.main.MainActivity
import com.foxyApp.foxy.splash.dagger.SplashModule
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), ISplashView {

    @BindView(R.id.splash_gradient)
    lateinit var mGradient: CustomCircle

    @BindView(R.id.splash_logo)
    lateinit var mLogo: ImageView

    @BindView(R.id.splash_app_name)
    lateinit var mAppName: TextView

    @BindView(R.id.splash_slogan)
    lateinit var mSlogan: TextView

    @Inject
    lateinit var mPresenter: ISplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)
        setFonts()
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(SplashModule())?.inject(this)
        mPresenter.setSessionToken()
        mPresenter.attachView(this)
        startFirstAnimation()
    }

    /**
     * Set fonts.
     */
    private fun setFonts() {
        val appTitle = Typeface.createFromAsset(assets, "fonts/appNameFont.otf")
        val boldItalic = Typeface.createFromAsset(assets, "fonts/SourceSansPro-SemiboldItalic.ttf")
        mAppName.typeface = appTitle
        mSlogan.typeface = boldItalic
    }

    /**
     * First animation : scaling from 0 to 1.
     */
    private fun startFirstAnimation() {
        val animationLoadLogo = AnimationUtils.loadAnimation(this, R.anim.scale_0_to_1)
        animationLoadLogo.duration = 500
        animationLoadLogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                startSecondAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        mLogo.startAnimation(animationLoadLogo)
    }

    /**
     * Second animation : scaling from 1 to 0.8
     */
    private fun startSecondAnimation() {
        val decreaseSize = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.scale1to08)
        decreaseSize.duration = 100
        decreaseSize.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                mGradient.startAnimation(mLogo.left + mLogo.width / 2, mLogo.top + mLogo.height / 2,
                        mGradient.measuredHeight / 6, mGradient.measuredHeight)
            }

            override fun onAnimationEnd(animation: Animation) {
                startLastAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mLogo.startAnimation(decreaseSize)
    }

    /**
     * Last animation : scaling from 0.8 to 1
     */
    private fun startLastAnimation() {
        val increaseSize = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.scale08to1)
        increaseSize.duration = 100
        increaseSize.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                Handler().postDelayed({
                    if (!mPresenter.getSessionToken().isEmpty()) {
                        startActivity(MainActivity.getStartingIntent(this@SplashActivity, false))
                    } else {
                        startActivity(ConnectActivity.getStartingIntent(this@SplashActivity))
                    }
                    finish()
                }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        mLogo.startAnimation(increaseSize)
    }
}
