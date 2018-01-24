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
import butterknife.BindView
import butterknife.ButterKnife
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer
import org.foxy.foxy.R
import org.foxy.foxy.ranking.global.RankingGlobalFragment

class RankingFragment : Fragment() {

    private var mView: View? = null
    private val mNbItem = 3
    private val mPositionGlobal = 0
    private val mPositionMonth = 1
    private val mPositionWeek = 2

    @BindView(R.id.ranking_view_pager)
    lateinit var mViewPager: ViewPager

    @BindView(R.id.ranking_tabs)
    lateinit var mTabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_ranking, container, false)
        ButterKnife.bind(this, mView!!)
        // setting up the view pager with the sections adapter.
        mViewPager.adapter = SectionsPagerAdapter(childFragmentManager)
        mViewPager.setPageTransformer(true, ZoomOutSlideTransformer())
        mTabLayout.setupWithViewPager(mViewPager)
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
                mPositionMonth -> fragment = RankingGlobalFragment()
                mPositionWeek -> fragment = RankingGlobalFragment()
            }
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                mPositionGlobal -> return getString(R.string.Global)
                mPositionMonth -> return getString(R.string.Month)
                mPositionWeek -> return getString(R.string.Week)
                else -> String()
            }
        }

        override fun getCount(): Int = mNbItem
    }

}
