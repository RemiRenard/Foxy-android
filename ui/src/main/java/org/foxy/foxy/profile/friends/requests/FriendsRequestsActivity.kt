package org.foxy.foxy.profile.friends.requests

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.adapter.FriendsRequestsAdapter
import org.foxy.foxy.custom.SimpleDividerItemDecoration
import org.foxy.foxy.event_bus.FriendRequestClickedEvent
import org.foxy.foxy.profile.dagger.ProfileModule
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Friends requests activity class.
 */
class FriendsRequestsActivity : BaseActivity(), IFriendsRequestsView {

    @BindView(R.id.friends_requests_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.friends_no_friends_requests)
    lateinit var mNoFriendsRequests: TextView

    @BindView(R.id.friends_requests_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @Inject
    lateinit var mPresenter: IFriendsRequestsPresenter

    @Inject
    lateinit var mAdapter: FriendsRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_requests)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(ProfileModule())?.inject(this)
        initRecyclerView()
        mPresenter.attachView(this)
        mPresenter.getFriendsRequests()
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFriendRequestClickedEvent(event: FriendRequestClickedEvent) {
        if (event.isAccepted) {
            mPresenter.acceptRequest(event.requestId, event.notificationId, event.request)
        } else {
            mPresenter.declineRequest(event.requestId, event.notificationId, event.request)
        }
    }

    @OnClick(R.id.toolbar_back)
    fun back() {
        onBackPressed()
    }

    override fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>) {
        mNoFriendsRequests.visibility = if (friendsRequests.isNotEmpty()) View.GONE else View.VISIBLE
        mAdapter.setData(friendsRequests)
    }

    override fun friendRequestCompleted(request: FriendsRequestsResponse) {
        mAdapter.removeItem(request)
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent = Intent(callingContext,
                FriendsRequestsActivity::class.java)
    }
}