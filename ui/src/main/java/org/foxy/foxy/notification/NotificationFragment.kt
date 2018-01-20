package org.foxy.foxy.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.data.Constants
import org.foxy.data.model.Notification
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.adapter.NotificationAdapter
import org.foxy.foxy.custom.SimpleDividerItemDecoration
import org.foxy.foxy.event_bus.NotificationClickedEvent
import org.foxy.foxy.notification.add.AddNotificationActivity
import org.foxy.foxy.notification.dagger.NotificationModule
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Notification fragment class.
 */
class NotificationFragment : Fragment(), INotificationView {

    private var mView: View? = null

    @BindView(R.id.notification_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.notification_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.notification_swipe_refresh_layout)
    lateinit var mSwipeRefresh: SwipeRefreshLayout

    @BindView(R.id.notification_none)
    lateinit var mNoNotification: TextView

    @Inject
    lateinit var mPresenter: INotificationPresenter

    @Inject
    lateinit var mAdapter: NotificationAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_notification, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context).getAppComponent()?.plus(NotificationModule())?.inject(this)
        initRecyclerView()
        mPresenter.attachView(this)
        mPresenter.getNotifications(arguments.getBoolean(Constants.BUNDLE_IS_NEW_NOTIFICATION, false))
        mSwipeRefresh.setOnRefreshListener {
            mPresenter.getNotifications(forceNetworkRefresh = true)
            mSwipeRefresh.isRefreshing = false
        }
        return mView
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(SimpleDividerItemDecoration(context))
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNotificationClickedEvent(event: NotificationClickedEvent) {
        mPresenter.markNotificationAsRead(event.notificationId)
    }

    override fun displayNotifications(notifications: List<Notification>) {
        mAdapter.setData(notifications)
        if (notifications.isNotEmpty()) {
            mNoNotification.visibility = View.GONE
        } else {
            mNoNotification.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
