package com.app.foxy.ranking.subFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.adapter.RankingAdapter
import com.app.foxy.custom.SimpleDividerItemDecoration
import com.app.foxy.eventBus.RankingCompleteEvent
import com.app.foxy.eventBus.RefreshRankingSwiped
import com.app.foxy.ranking.dagger.RankingModule
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class RankingWeeklyFragment : Fragment() {

    private var mView: View? = null

    @BindView(R.id.ranking_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.ranking_swipe_refresh_layout)
    lateinit var mSwipeRefresh: SwipeRefreshLayout

    @BindView(R.id.ranking_content_no_data)
    lateinit var mNoData: TextView

    @Inject
    lateinit var mAdapter: RankingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_ranking_content, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context!!).getAppComponent()?.plus(RankingModule())?.inject(this)
        initRecyclerView()
        EventBus.getDefault().register(this)
        mSwipeRefresh.setOnRefreshListener {
            EventBus.getDefault().post(RefreshRankingSwiped())
            mSwipeRefresh.isRefreshing = false
        }
        return mView
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginViewClickedEvent(event: RankingCompleteEvent) {
        if (event.rankingResponse.weeklyRanking.isNotEmpty()) {
            mNoData.visibility = View.GONE
        } else {
            mNoData.visibility = View.VISIBLE
        }
        mAdapter.setData(event.rankingResponse.weeklyRanking)
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(SimpleDividerItemDecoration(context!!))
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }
}
