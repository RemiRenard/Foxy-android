package com.app.foxy.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.data.Constants
import com.app.foxy.BaseActivity
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.eventBus.CameraPermsResultEvent
import com.app.foxy.eventBus.WriteStoragePermResultEvent
import com.app.foxy.friends.FriendsFragment
import com.app.foxy.main.dagger.MainModule
import com.app.foxy.notification.NotificationFragment
import com.app.foxy.notification.selectSong.SelectSongActivity
import com.app.foxy.profile.ProfileFragment
import com.app.foxy.ranking.RankingFragment
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


/**
 * Main activity
 */
class MainActivity : BaseActivity(), IMainView {

    private var mIsNewNotification: Boolean = false

    @BindView(R.id.notif_send_button)
    lateinit var mSendNotifButton: FloatingActionButton

    @BindView(R.id.main_view_pager)
    lateinit var mViewPager: ViewPager

    @Inject
    lateinit var mPresenter: IMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mIsNewNotification = intent.getBooleanExtra(Constants.EXTRA_IS_NEW_NOTIFICATION, false)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(MainModule())?.inject(this)
        mPresenter.attachView(this)
        // Binding views.
        ButterKnife.bind(this)
        if (mIsNewNotification) {
            mPresenter.refreshToken()
        }
        // setting up the view pager with the sections adapter.
        mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
        mViewPager.setPageTransformer(true, ZoomOutSlideTransformer())
        manageFragment()
        mPresenter.manageTutorial()
    }

    override fun showTutorial() {
        mSendNotifButton.isClickable = false
        TapTargetSequence(this).targets(
                TapTarget.forView(mSendNotifButton,
                        getString(R.string.tuto_btn_send_notif), getString(R.string.tuto_btn_send_notif_desc))
                        .transparentTarget(true)
                        .outerCircleColor(android.R.color.white)
                        .textColor(R.color.colorPrimary)
                        .targetRadius(25)
                        .targetCircleColor(R.color.colorPrimaryDark)
        ).listener(object : TapTargetSequence.Listener {
            override fun onSequenceCanceled(lastTarget: TapTarget?) {
                // Do nothing
            }

            override fun onSequenceFinish() {
                // Do nothing
            }

            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                mSendNotifButton.isClickable = true
                mViewPager.currentItem = FRIENDS_FRAGMENT_POSITION
            }
        }).continueOnCancel(true).start()
    }

    @OnClick(R.id.notif_send_button)
    fun addNotification() {
        startActivity(SelectSongActivity.getStartingIntent(this))
    }

    /**
     * Display the good fragment if there are new notifications
     */
    private fun manageFragment() {
        val fragment: Fragment = NotificationFragment()
        val bundle = Bundle()
        bundle.putBoolean(Constants.BUNDLE_IS_NEW_NOTIFICATION, mIsNewNotification)
        fragment.arguments = bundle
        mViewPager.currentItem = NOTIFICATION_FRAGMENT_POSITION
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            EventBus.getDefault().post(WriteStoragePermResultEvent(grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED))
        }
        if (requestCode == Constants.REQUEST_PERMISSION_GROUP_CAMERA) {
            EventBus.getDefault().post(CameraPermsResultEvent(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED))
        }
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null
            when (position) {
                PROFILE_FRAGMENT_POSITION -> fragment = ProfileFragment()
                FRIENDS_FRAGMENT_POSITION -> fragment = FriendsFragment()
                NOTIFICATION_FRAGMENT_POSITION -> fragment = NotificationFragment()
                RANKING_FRAGMENT_POSITION -> fragment = RankingFragment()
            }
            fragment?.arguments = Bundle()
            return fragment
        }

        override fun getCount(): Int = NB_PAGE
    }


    companion object {

        private val PROFILE_FRAGMENT_POSITION = 0
        private val FRIENDS_FRAGMENT_POSITION = 1
        private val NOTIFICATION_FRAGMENT_POSITION = 2
        private val RANKING_FRAGMENT_POSITION = 3
        private val NB_PAGE = 4

        /**
         * Return the intent to start this activity.
         * @param callingContext    Context
         * @param isNewNotification boolean
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context, isNewNotification: Boolean): Intent {
            val intent = Intent(callingContext, MainActivity::class.java)
            intent.putExtra(Constants.EXTRA_IS_NEW_NOTIFICATION, isNewNotification)
            return intent
        }
    }
}
