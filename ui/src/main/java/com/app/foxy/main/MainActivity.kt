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
import android.support.v7.widget.CardView
import android.view.View
import android.widget.Toast
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
    private var mIsMenuOpen: Boolean = false

    @BindView(R.id.main_view_pager)
    lateinit var mViewPager: ViewPager

    @BindView(R.id.navigation_notification_record)
    lateinit var mNavRecord: FloatingActionButton

    @BindView(R.id.navigation_notification_list)
    lateinit var mNavListSound: FloatingActionButton

    @BindView(R.id.navigation_rank)
    lateinit var mNavRank: FloatingActionButton

    @BindView(R.id.navigation_notification)
    lateinit var mNavNotifications: FloatingActionButton

    @BindView(R.id.navigation_friends)
    lateinit var mNavFriends: FloatingActionButton

    @BindView(R.id.navigation_profile)
    lateinit var mNavProfile: FloatingActionButton

    @BindView(R.id.navigation_button)
    lateinit var mNavButton: FloatingActionButton

    @BindView(R.id.navigation_profile_card)
    lateinit var mNavProfileCard: CardView

    @BindView(R.id.navigation_friends_card)
    lateinit var mNavFriendsCard: CardView

    @BindView(R.id.navigation_notification_card)
    lateinit var mNavNotificationsCard: CardView

    @BindView(R.id.navigation_rank_card)
    lateinit var mNavRankCard: CardView

    @BindView(R.id.navigation_notification_list_card)
    lateinit var mNavListSoundCard: CardView

    @BindView(R.id.navigation_notification_record_card)
    lateinit var mNavRecordCard: CardView

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
        mViewPager.currentItem = NOTIFICATION_FRAGMENT_POSITION
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // Do nothing.
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Do nothing.
            }

            override fun onPageSelected(position: Int) {
                mIsMenuOpen = false
                showMenu(false)
            }
        })
        mPresenter.manageTutorial()
    }

    override fun showTutorial() {
        mNavButton.isClickable = false
        TapTargetSequence(this).targets(
                TapTarget.forView(mNavButton,
                        getString(R.string.tuto_btn_nav), getString(R.string.tuto_btn_nav_desc))
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
                mNavButton.isClickable = true
                mViewPager.currentItem = FRIENDS_FRAGMENT_POSITION
            }
        }).continueOnCancel(true).start()
    }

    @OnClick(R.id.navigation_button)
    fun navigationClicked() {
        mIsMenuOpen = !mIsMenuOpen
        showMenu(mIsMenuOpen)
    }

    private fun showMenu(isShowing: Boolean) {
        mNavButton.animate().rotation(if (isShowing) 180F else 0F)
        // Cards
        mNavRecordCard.visibility = if (isShowing) View.VISIBLE else View.GONE
        mNavRecordCard.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_70) else 0F)
        mNavListSoundCard.visibility = if (isShowing) View.VISIBLE else View.GONE
        mNavListSoundCard.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_120) else 0F)
        mNavRankCard.visibility = if (isShowing) View.VISIBLE else View.GONE
        mNavRankCard.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_170) else 0F)
        mNavNotificationsCard.visibility = if (isShowing) View.VISIBLE else View.GONE
        mNavNotificationsCard.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_220) else 0F)
        mNavFriendsCard.visibility = if (isShowing) View.VISIBLE else View.GONE
        mNavFriendsCard.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_270) else 0F)
        mNavProfileCard.visibility = if (isShowing) View.VISIBLE else View.GONE
        mNavProfileCard.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_320) else 0F)
        // Floating buttons
        mNavRecord.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_70) else 0F)
        mNavListSound.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_120) else 0F)
        mNavRank.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_170) else 0F)
        mNavNotifications.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_220) else 0F)
        mNavFriends.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_270) else 0F)
        mNavProfile.animate().translationY(if (isShowing) -resources.getDimension(R.dimen.standard_320) else 0F)
    }

    @OnClick(R.id.navigation_friends)
    fun navToFriends() {
        mViewPager.currentItem = FRIENDS_FRAGMENT_POSITION
    }

    @OnClick(R.id.navigation_notification)
    fun navToNotifications() {
        mViewPager.currentItem = NOTIFICATION_FRAGMENT_POSITION
    }

    @OnClick(R.id.navigation_profile)
    fun navToProfile() {
        mViewPager.currentItem = PROFILE_FRAGMENT_POSITION
    }

    @OnClick(R.id.navigation_rank)
    fun navToRanking() {
        mViewPager.currentItem = RANKING_FRAGMENT_POSITION
    }

    @OnClick(R.id.navigation_notification_list)
    fun navToListSound() {
        startActivity(SelectSongActivity.getStartingIntent(this))
    }

    @OnClick(R.id.navigation_notification_record)
    fun navToRecordSound() {
        Toast.makeText(this, "Not implemented yet :/", Toast.LENGTH_SHORT).show()
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
            val bundle = Bundle()
            bundle.putBoolean(Constants.BUNDLE_IS_NEW_NOTIFICATION, mIsNewNotification)
            fragment?.arguments = bundle
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
