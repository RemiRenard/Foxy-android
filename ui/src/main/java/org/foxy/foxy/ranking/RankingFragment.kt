package org.foxy.foxy.ranking

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.ranking.dagger.RankingModule
import org.foxy.foxy.ranking.global.RankingGlobalFragment
import javax.inject.Inject

class RankingFragment : Fragment(), IRankingView {
    private var mView: View? = null
    private val mNbItem = 3
    private val mPositionGlobal = 0
    private val mPositionWeekly = 1
    private val mPositionDaily = 2

    @BindView(R.id.ranking_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.ranking_view_pager)
    lateinit var mViewPager: ViewPager

    @BindView(R.id.ranking_tabs)
    lateinit var mTabLayout: TabLayout

    @BindView(R.id.ranking_profile_avatar)
    lateinit var mProfileAvatar: ImageView

    @Inject
    lateinit var mPresenter: IRankingPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_ranking, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context).getAppComponent()?.plus(RankingModule())?.inject(this)
        // setting up the view pager with the sections adapter.
        mViewPager.adapter = SectionsPagerAdapter(childFragmentManager)
        mTabLayout.setupWithViewPager(mViewPager)

        mPresenter.attachView(this)

        return mView
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null
            when (position) {
                mPositionGlobal -> fragment = RankingGlobalFragment()
                mPositionWeekly -> fragment = RankingGlobalFragment()
                mPositionDaily -> fragment = RankingGlobalFragment()
            }
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                mPositionGlobal -> return getString(R.string.Global)
                mPositionWeekly -> return getString(R.string.Weekly)
                mPositionDaily -> return getString(R.string.Daily)
                else -> String()
            }
        }

        override fun getCount(): Int = mNbItem
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {

        mProgressBar.visibility = View.GONE

    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
