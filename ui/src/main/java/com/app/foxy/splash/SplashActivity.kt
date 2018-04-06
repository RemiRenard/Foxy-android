package com.app.foxy.splash

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.app.foxy.BaseActivity
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.connect.ConnectActivity
import com.app.foxy.custom.CustomCircle
import com.app.foxy.main.MainActivity
import com.app.foxy.splash.dagger.SplashModule
import com.app.foxy.update.ForceUpdateActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(), ISplashView {

    private var mIsVersionCorrect = false
    private var mIsAnimationCompleted = false
    private var mNetworkCallCompleted = false

    @BindView(R.id.splash_gradient)
    lateinit var mGradient: CustomCircle

    @BindView(R.id.splash_logo)
    lateinit var mLogo: ImageView

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
        mPresenter.checkConfig()
        startFirstAnimation()
    }

    /**
     * Set fonts.
     */
    private fun setFonts() {
        val appTitle = Typeface.createFromAsset(assets, "fonts/appNameFont.otf")
        val boldItalic = Typeface.createFromAsset(assets, "fonts/SourceSansPro-SemiboldItalic.ttf")
        // TODO add app name
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
        decreaseSize.duration = 300
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
        increaseSize.duration = 300
        increaseSize.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                Handler().postDelayed({
                    mIsAnimationCompleted = true
                    manageNextScreen()
                }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        mLogo.startAnimation(increaseSize)
    }

    private fun manageNextScreen() {
        if (mIsVersionCorrect && mIsAnimationCompleted) {
            if (!mPresenter.getSessionToken().isEmpty()) {
                startActivity(MainActivity.getStartingIntent(this@SplashActivity, false))
            } else {
                startActivity(ConnectActivity.getStartingIntent(this@SplashActivity))
            }
            finish()
        } else if (!mIsVersionCorrect && mIsAnimationCompleted && mNetworkCallCompleted) {
            startActivity(ForceUpdateActivity.getStartingIntent(this@SplashActivity))
            finish()
        } else if (!mNetworkCallCompleted) {
            Toast.makeText(this, R.string.We_are_checking_the_version, Toast.LENGTH_LONG).show()
        }
    }

    override fun forceUpdate(isVersionCorrect: Boolean) {
        mNetworkCallCompleted = true
        mIsVersionCorrect = isVersionCorrect
        manageNextScreen()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}
