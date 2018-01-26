package org.foxy.foxy.ranking.global

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import org.foxy.foxy.R
import org.foxy.foxy.adapter.RankingAdapter
import org.foxy.foxy.custom.SimpleDividerItemDecoration
import javax.inject.Inject

class RankingGlobalFragment : Fragment() {

    private var mView: View? = null

    @BindView(R.id.ranking_global_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.ranking_global_swipe_refresh_layout)
    lateinit var mSwipeRefresh: SwipeRefreshLayout

    @Inject
    lateinit var mAdapter: RankingAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_ranking_global, container, false)
        ButterKnife.bind(this, mView!!)
        initRecyclerView()
        mSwipeRefresh.setOnRefreshListener {
            mSwipeRefresh.isRefreshing = false
        }
        return mView
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(SimpleDividerItemDecoration(context))
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = RankingAdapter(context)
        mRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

}
