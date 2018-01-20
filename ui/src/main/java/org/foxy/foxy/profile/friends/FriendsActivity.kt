package org.foxy.foxy.profile.friends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.data.model.User
import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.adapter.FriendsAdapter
import org.foxy.foxy.profile.dagger.ProfileModule
import org.foxy.foxy.profile.friends.add.AddFriendsActivity
import org.foxy.foxy.profile.friends.requests.FriendsRequestsActivity
import javax.inject.Inject

/**
 * Friends activity class.
 */
class FriendsActivity : BaseActivity(), IFriendsView {

    private var mScaleAnimation: Animation? = null

    @BindView(R.id.friends_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.friends_no_friends)
    lateinit var mNoFriends: TextView

    @BindView(R.id.friends_point_up)
    lateinit var mCurvedArrow: ImageView

    @BindView(R.id.friends_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.friends_banner_requests)
    lateinit var mBannerRequests: TextView

    @Inject
    lateinit var mPresenter: IFriendsPresenter

    @Inject
    lateinit var mAdapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(ProfileModule())?.inject(this)
        initRecyclerView()
        // Animation
        mScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        mScaleAnimation?.repeatMode = Animation.REVERSE
        mScaleAnimation?.repeatCount = -1
        mPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getFriends()
        mPresenter.getFriendsRequests()
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }

    @OnClick(R.id.toolbar_add)
    fun addFriends() {
        startActivity(AddFriendsActivity.getStartingIntent(this))
    }

    @OnClick(R.id.toolbar_cancel)
    fun cancel() {
        onBackPressed()
    }

    @OnClick(R.id.friends_banner_requests)
    fun openFriendsRequests() {
        startActivity(FriendsRequestsActivity.getStartingIntent(this))
    }

    override fun displayFriends(friends: List<User>) {
        mAdapter.setData(friends)
        mNoFriends.visibility = if (friends.isNotEmpty()) View.GONE else View.VISIBLE
        mCurvedArrow.clearAnimation()
        if (friends.isEmpty()) {
            mCurvedArrow.startAnimation(mScaleAnimation)
            mCurvedArrow.visibility = View.VISIBLE
        } else {
            mCurvedArrow.visibility = View.GONE
        }
    }

    override fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>) {
        if (friendsRequests.isNotEmpty()) {
            mBannerRequests.text = getString(R.string.placeholder_friend_request, friendsRequests.size)
            mBannerRequests.visibility = View.VISIBLE
            mCurvedArrow.clearAnimation()
            mCurvedArrow.visibility = View.GONE
        } else {
            mBannerRequests.visibility = View.GONE
        }
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
                FriendsActivity::class.java)
    }
}