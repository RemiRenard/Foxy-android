package com.app.foxy.main

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.app.foxy.notification.add.AddNotificationActivity
import com.app.foxy.profile.ProfileFragment
import com.app.foxy.ranking.RankingFragment
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


/**
 * Main activity
 */
class MainActivity : BaseActivity(), IMainView {

    private var mFragmentManager: FragmentManager? = null
    private var mIsNewNotification: Boolean = false
    private var mCurrentViewId: Int = 0

    @BindView(R.id.notif_send_button)
    lateinit var mSendNotifButton: FloatingActionButton

    @BindView(R.id.navigation_friends)
    lateinit var mFriendsNav: ImageView

    @BindView(R.id.bottom_bar)
    lateinit var mBottomBarLayout: LinearLayout

    @BindView(R.id.navigation_notification)
    lateinit var mNavigationNotification: ImageView

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
        mFragmentManager = supportFragmentManager
        if (mIsNewNotification) {
            mPresenter.refreshToken()
        }
        manageColorBottomBar(mNavigationNotification)
        manageFragment()
        showTutorial()
    }

    /**
     * Show the tutorial.
     */
    private fun showTutorial() {
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
                // Dp nothing.
            }

            override fun onSequenceFinish() {
                // Do nothing for now.
            }

            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                // Do nothing.
            }
        }).start()
    }

    @OnClick(R.id.notif_send_button)
    fun addNotification() {
        startActivity(AddNotificationActivity.getStartingIntent(this))
    }

    /**
     * Display the good fragment if there are new notifications
     */
    private fun manageFragment() {
        val fragment: Fragment = NotificationFragment()
        val bundle = Bundle()
        bundle.putBoolean(Constants.BUNDLE_IS_NEW_NOTIFICATION, mIsNewNotification)
        fragment.arguments = bundle
        mFragmentManager?.beginTransaction()?.replace(R.id.content, fragment)?.commit()
    }

    @OnClick(R.id.navigation_profile, R.id.navigation_notification, R.id.navigation_rank, R.id.navigation_friends)
    fun manageBottomBar(view: View) {
        manageColorBottomBar(view)
        var fragment: Fragment? = null
        when (view.id) {
            R.id.navigation_notification -> fragment = NotificationFragment()
            R.id.navigation_profile -> fragment = ProfileFragment()
            R.id.navigation_rank -> fragment = RankingFragment()
            R.id.navigation_friends -> fragment = FriendsFragment()
        }
        val bundle = Bundle()
        fragment?.arguments = bundle
        mFragmentManager?.popBackStack()
        if (fragment != null && mCurrentViewId != view.id) {
            mFragmentManager?.beginTransaction()?.replace(R.id.content, fragment)?.commit()
        }
        ObjectAnimator.ofFloat(view, "translationY", 0f, -25f, 0f).setDuration(500).start()
        mCurrentViewId = view.id
    }

    /**
     * Manage the color of the bottom bar.
     * @param viewClicked View
     */
    private fun manageColorBottomBar(viewClicked: View?) {
        (0 until mBottomBarLayout.childCount)
                .map { mBottomBarLayout.getChildAt(it) as ImageView }
                .forEach {
                    if (viewClicked != null && it.id == viewClicked.id) {
                        it.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.DST)
                    } else {
                        it.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryTransparent))
                    }
                }
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

    companion object {

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
