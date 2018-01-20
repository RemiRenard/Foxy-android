package org.foxy.foxy.profile.friends.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.foxy.data.model.User
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.adapter.AddFriendsAdapter
import org.foxy.foxy.event_bus.AddFriendsIconClickedEvent
import org.foxy.foxy.profile.dagger.ProfileModule
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * AddFriends activity class.
 */
class AddFriendsActivity : BaseActivity(), IAddFriendsView {

    @BindView(R.id.add_friends_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.add_friends_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.add_friends_search_bar)
    lateinit var mSearchBar: EditText

    @BindView(R.id.add_friends_none)
    lateinit var mNoFriends: TextView

    @Inject
    lateinit var mAdapter: AddFriendsAdapter

    @Inject
    lateinit var mPresenter: IAddFriendsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(ProfileModule())?.inject(this)
        initRecyclerView()
        mPresenter.attachView(this)
        //Reactive search bar
        RxTextView.textChanges(mSearchBar)
                .filter({ mSearchBar.text.length > 2 }) //From more than 2 characters
                .debounce(300, TimeUnit.MILLISECONDS) //If more than 300 milliseconds passed after a character entry
                .map { it.toString() } //charSequence to String
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { "" }
                .subscribe { mPresenter.findUsers(it) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddFriendsIconClickedEvent(event: AddFriendsIconClickedEvent) {
        mPresenter.addFriends(event.user)
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }

    override fun displayUsers(users: List<User>) {
        mNoFriends.visibility = if (users.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mAdapter.setData(users)
    }

    @OnClick(R.id.toolbar_back)
    fun back() {
        onBackPressed()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun addFriendsComplete(user: User) {
        mAdapter.requestSent(user)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent = Intent(callingContext,
                AddFriendsActivity::class.java)
    }
}