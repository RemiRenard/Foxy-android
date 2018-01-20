package org.foxy.foxy.profile

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.foxy.data.model.User
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.profile.dagger.ProfileModule
import org.foxy.foxy.profile.friends.FriendsActivity
import org.foxy.foxy.profile.settings.SettingsActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileFragment : Fragment(), IProfileView {

    private var mView: View? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private val ACTIVITY_RESULT_LIBRARY = 1
    private val ACTIVITY_RESULT_CAMERA = 2
    private var mCameraFile: File? = null
    private var mCurrentUser: User? = null
    private var mDatePicker: DatePickerDialog.OnDateSetListener? = null
    private val mDatePattern: String = "yyyy-MM-dd"
    private var mCalendar: Calendar? = null

    @BindView(R.id.profile_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.swipe_refresh_profile)
    lateinit var mSwipeRefresh: SwipeRefreshLayout

    @BindView(R.id.profile_avatar)
    lateinit var mProfileAvatar: ImageView

    @BindView(R.id.profile_name)
    lateinit var mProfileName: TextView

    @BindView(R.id.profile_username)
    lateinit var mProfileUsername: TextView

    @BindView(R.id.profile_chart_time_played)
    lateinit var mChart: BarChart

    @BindView(R.id.profile_chart_best_friends)
    lateinit var mPieChart: PieChart

    @BindView(R.id.card_stats_profile_nb_drink_given_text_view)
    lateinit var mNbDrinkGiven: TextView

    @BindView(R.id.card_stats_profile_nb_drink_finished_text_view)
    lateinit var mNbDrinkFinished: TextView

    @BindView(R.id.card_stats_profile_nb_game_played_text_view)
    lateinit var mNbGamePlayed: TextView

    @BindView(R.id.profile_view_under_bar_chart)
    lateinit var mViewBarChart: View

    @BindView(R.id.profile_chart_top_friends_title)
    lateinit var mTopFriendsTitle: TextView

    @Inject
    lateinit var mPresenter: IProfilePresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_profile, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context).getAppComponent()?.plus(ProfileModule())?.inject(this)
        mPresenter.attachView(this)
        mPresenter.getProfile(forceNetworkRefresh = false)
        mSwipeRefresh.setOnRefreshListener { mPresenter.getProfile(forceNetworkRefresh = true) }
        displayBarChart()
        displayPieChart()
        return mView
    }

    @OnClick(R.id.profile_see_more_stats)
    fun seeMoreStats(view: TextView) {
        if (TextUtils.equals(view.text, getString(R.string.See_more_stats))) {
            mViewBarChart.visibility = View.VISIBLE
            mPieChart.visibility = View.VISIBLE
            mTopFriendsTitle.visibility = View.VISIBLE
            view.text = getString(R.string.See_less_stats)
        } else {
            view.text = getString(R.string.See_more_stats)
            mViewBarChart.visibility = View.GONE
            mPieChart.visibility = View.GONE
            mTopFriendsTitle.visibility = View.GONE
        }
    }

    /**
     * Display the bar chart (time played for each game)
     */
    private fun displayBarChart() {
        mChart.description.isEnabled = false
        mChart.setPinchZoom(false)
        mChart.setDrawBarShadow(false)
        mChart.setDrawGridBackground(false)
        mChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        mChart.xAxis.setDrawGridLines(false)
        mChart.getAxis(YAxis.AxisDependency.LEFT).setDrawGridLines(false)
        mChart.legend.isEnabled = false
        mChart.setFitBars(true)
    }

    /**
     * Display the pie chart (top friends)
     */
    private fun displayPieChart() {
        mPieChart.description.isEnabled = false
        mPieChart.setHoleColor(Color.TRANSPARENT)
        mPieChart.setTransparentCircleColor(Color.WHITE)
        mPieChart.setTransparentCircleAlpha(110)
        mPieChart.holeRadius = 45f
        mPieChart.setEntryLabelColor(Color.BLACK)
        mPieChart.transparentCircleRadius = 20f
        mPieChart.setDrawCenterText(true)
        mPieChart.rotationAngle = 0F
        // enable rotation of the chart by touch
        mPieChart.isRotationEnabled = true
        mPieChart.legend.isEnabled = false
        mPieChart.isHighlightPerTapEnabled = true
    }

    /**
     * Show profile information
     */
    override fun showProfileInformation(user: User) {
        // TODO Get the list of games & top friends with the server
        setBarChartData()
        setPieChartData()
        mCurrentUser = user
        if (mCurrentUser!!.avatar != null) {
            Glide.with(context).load(mCurrentUser?.avatar).apply(RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.ic_placeholder_circle_gray))
                    .into(mProfileAvatar)
        }
        mProfileName.text = context.getString(R.string.placeholder_name, mCurrentUser?.firstName, mCurrentUser?.lastName)
        mProfileUsername.text = mCurrentUser?.username

        //TODO call server instead of hardcoded text
        mNbDrinkGiven.text = 32.toString()
        mNbDrinkFinished.text = 16.toString()
        mNbGamePlayed.text = 12.toString()
    }

    /**
     * Add data to the bar chart
     */
    private fun setBarChartData() {
        // TODO Get the list of games with the server
        val nbGames = 5
        val games = ArrayList<String>()
        val yVal = ArrayList<BarEntry>()
        for (i in 0 until nbGames) {
            val value = (Math.random() * nbGames) + 15
            yVal.add(BarEntry(i.toFloat(), value.toFloat()))
            games.add("Game" + i)
        }
        val set = BarDataSet(yVal, "Games")
        val colors: MutableList<Int> = ArrayList()
        ColorTemplate.VORDIPLOM_COLORS.forEach { colors.add(it) }
        set.colors = colors
        set.setDrawValues(false)
        val data = BarData(set)
        mChart.data = data
        mChart.xAxis.valueFormatter = IndexAxisValueFormatter(games)
        mChart.invalidate()
        mChart.animateY(500)
    }

    /**
     * Add data to the pie chart
     */
    private fun setPieChartData() {
        // TODO Get the list of top friends with the server
        val yVal = ArrayList<PieEntry>()
        for (i in 0 until 5) {
            val value = i * i + 10
            yVal.add(PieEntry(value.toFloat(), "Friends " + i))
        }
        val set = PieDataSet(yVal, "Top friends")
        val colors: MutableList<Int> = ArrayList()
        ColorTemplate.VORDIPLOM_COLORS.forEach { colors.add(it) }
        set.colors = colors
        set.setDrawValues(false)
        val data = PieData(set)
        mPieChart.data = data
        mPieChart.invalidate()
    }

    @OnClick(R.id.toolbar_settings)
    fun settingsClicked() {
        startActivity(SettingsActivity.getStartingIntent(context))
    }

    @OnClick(R.id.toolbar_friends)
    fun friendsClicked() {
        startActivity(FriendsActivity.getStartingIntent(context))
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
        mSwipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
