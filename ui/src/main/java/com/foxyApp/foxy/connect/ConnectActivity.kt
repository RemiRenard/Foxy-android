package com.foxyApp.foxy.connect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer
import com.foxyApp.foxy.BaseActivity
import com.foxyApp.foxy.R
import com.foxyApp.foxy.connect.fragment.LoginFragment
import com.foxyApp.foxy.connect.fragment.SignupFragment
import com.foxyApp.foxy.event_bus.ConnectStepCompleteEvent
import com.foxyApp.foxy.event_bus.CreateAccountViewClickedEvent
import com.foxyApp.foxy.event_bus.LoginViewClickedEvent
import com.foxyApp.foxy.main.MainActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ConnectActivity : BaseActivity() {

    @BindView(R.id.connect_view_pager)
    lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        ButterKnife.bind(this)
        // setting up the view pager with the sections adapter.
        mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
        mViewPager.setPageTransformer(true, ZoomOutSlideTransformer())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCreateAccountViewClickedEvent(event: CreateAccountViewClickedEvent) {
        mViewPager.currentItem = SIGNUP_FRAGMENT_POSITION
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginViewClickedEvent(event: LoginViewClickedEvent) {
        mViewPager.currentItem = LOGIN_FRAGMENT_POSITION
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onConnectStepCompleteEvent(event: ConnectStepCompleteEvent) {
        startActivity(MainActivity.getStartingIntent(this, false))
        finish()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null
            when (position) {
                LOGIN_FRAGMENT_POSITION -> fragment = LoginFragment()
                SIGNUP_FRAGMENT_POSITION -> fragment = SignupFragment()
            }
            return fragment
        }

        override fun getCount(): Int = NB_PAGE
    }

    companion object {

        private val LOGIN_FRAGMENT_POSITION = 0
        private val SIGNUP_FRAGMENT_POSITION = 1
        private val NB_PAGE = 2

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent =
                Intent(callingContext, ConnectActivity::class.java)
    }
}
