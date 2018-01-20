package org.foxy.foxy.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import org.foxy.data.model.Game
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.adapter.GameAdapter
import org.foxy.foxy.custom.SimpleDividerItemDecoration
import org.foxy.foxy.search.dagger.SearchModule
import javax.inject.Inject

class SearchFragment : Fragment(), ISearchView {

    private var mView: View? = null

    @BindView(R.id.search_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.search_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.search_swipe_refresh_layout)
    lateinit var mSwipeRefresh: SwipeRefreshLayout

    @Inject
    lateinit var mAdapter: GameAdapter

    @Inject
    lateinit var mPresenter: ISearchPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_search, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context).getAppComponent()?.plus(SearchModule())?.inject(this)
        initRecyclerView()
        mPresenter.attachView(this)
        mPresenter.getGames(false)
        mSwipeRefresh.setOnRefreshListener {
            mPresenter.getGames(forceNetworkRefresh = true)
            mSwipeRefresh.isRefreshing = false
        }
        return mView
    }

    /**
     * Initialize recyclerView.
     */
    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun displayGames(games: List<Game>) {
        mAdapter.setData(games)
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
