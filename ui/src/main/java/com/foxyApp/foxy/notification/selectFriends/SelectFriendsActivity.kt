package com.foxyApp.foxy.notification.selectFriends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.foxyApp.data.model.User
import com.foxyApp.foxy.BaseActivity
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.adapter.SelectFriendsAdapter
import com.foxyApp.foxy.custom.SimpleDividerItemDecoration
import com.foxyApp.foxy.eventBus.FriendsSelectedNotifEvent
import com.foxyApp.foxy.notification.dagger.NotificationModule
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject

/**
 * SelectFriendsActivity class.
 */
class SelectFriendsActivity : BaseActivity(), ISelectFriendsView {

    @BindView(R.id.select_friends_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.select_friends_button_send)
    lateinit var mButtonSend: Button

    @BindView(R.id.select_friends_no_friends)
    lateinit var mNoFriends: TextView

    @BindView(R.id.select_friends_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @Inject
    lateinit var mPresenterSelect: ISelectFriendsPresenter

    @Inject
    lateinit var mAdapter: SelectFriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_friends)
        ButterKnife.bind(this)
        mButtonSend.visibility = View.GONE
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(NotificationModule())?.inject(this)
        mPresenterSelect.attachView(this)
        // init recyclerView.
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        mRecyclerView.adapter = mAdapter
        // Load friends
        mPresenterSelect.getFriends()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFriendsSelectedNotifUpdateEvent(event: FriendsSelectedNotifEvent) {
        mButtonSend.visibility = if (event.friendsSelected.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun displayFriends(friends: List<User>) {
        mAdapter.setData(friends)
        mNoFriends.visibility = if (friends.isNotEmpty()) View.GONE else View.VISIBLE
    }

    @OnClick(R.id.select_friends_button_send)
    fun sendNotification() {
        val userIds = ArrayList<String>()
        mAdapter.friendsSelected.mapTo(userIds) { it.id!! }
        mPresenterSelect.sendNotification(userIds)
    }

    override fun notificationSent() {
        hideProgressBar()
        onBackPressed()
    }

    override fun enableButton(isEnable: Boolean) {
        mButtonSend.isEnabled = isEnable
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        mPresenterSelect.clearNotificationCache()
    }

    override fun onDestroy() {
        mPresenterSelect.detachView()
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent =
                Intent(callingContext, SelectFriendsActivity::class.java)
    }
}
