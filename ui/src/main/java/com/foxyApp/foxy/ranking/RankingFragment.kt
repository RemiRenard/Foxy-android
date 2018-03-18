package com.foxyApp.foxy.ranking

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
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.foxyApp.data.model.UserRank
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.ranking.dagger.RankingModule
import com.foxyApp.foxy.ranking.subFragment.RankingDailyFragment
import com.foxyApp.foxy.ranking.subFragment.RankingGlobalFragment
import com.foxyApp.foxy.ranking.subFragment.RankingWeeklyFragment
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

    @BindView(R.id.ranking_score)
    lateinit var mScore: TextView

    @BindView(R.id.ranking_rank)
    lateinit var mRank: TextView

    @Inject
    lateinit var mPresenter: IRankingPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_ranking, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context!!).getAppComponent()?.plus(RankingModule())?.inject(this)
        // setting up the view pager with the sections adapter.
        mViewPager.adapter = SectionsPagerAdapter(childFragmentManager)
        mViewPager.offscreenPageLimit = mNbItem
        mTabLayout.setupWithViewPager(mViewPager)
        mPresenter.attachView(this)
        mPresenter.getRanking()
        return mView
    }

    override fun showCurrentUserData(currentUserData: UserRank) {
        Glide.with(context)
                .load(currentUserData.avatar)
                .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_placeholder_circle_blue))
                .into(mProfileAvatar)
        if (currentUserData.score != null) {
            mScore.text = context!!.getString(R.string.Score, currentUserData.score)
        } else {
            mScore.text = getString(R.string.Unranked)
        }
        if (currentUserData.rank != null) {
            mRank.text = getRankString(currentUserData.rank!!)
        } else {
            mRank.text = getString(R.string.No_points)
        }
    }

    /**
     * return the string of the rank + its ending ('st', 'nd', 'rd' or 'th')
     */
    private fun getRankString(n: Int): String {
        if (n in 11..13) {
            return n.toString() + "th"
        }
        return when (n % 10) {
            1 -> n.toString() + "st"
            2 -> n.toString() + "nd"
            3 -> n.toString() + "rd"
            else -> n.toString() + "th"
        }
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
                mPositionWeekly -> fragment = RankingWeeklyFragment()
                mPositionDaily -> fragment = RankingDailyFragment()
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
