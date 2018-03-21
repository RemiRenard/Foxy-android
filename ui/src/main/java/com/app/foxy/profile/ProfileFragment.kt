package com.app.foxy.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.app.data.Constants
import com.app.data.model.Stats
import com.app.data.model.User
import com.app.foxy.BuildConfig
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.eventBus.CameraPermsResultEvent
import com.app.foxy.eventBus.WriteStoragePermResultEvent
import com.app.foxy.profile.dagger.ProfileModule
import com.app.foxy.profile.settings.SettingsActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Profile fragment class.
 */
class ProfileFragment : Fragment(), IProfileView {

    private var mView: View? = null
    private var mCurrentUser: User? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private val ACTIVITY_RESULT_LIBRARY = 1
    private val ACTIVITY_RESULT_CAMERA = 2
    private var mCameraFile: File? = null

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

    @BindView(R.id.profile_chart_top_songs_played)
    lateinit var mChart: BarChart

    @BindView(R.id.profile_chart_best_friends)
    lateinit var mPieChart: PieChart

    @BindView(R.id.profile_chart_top_songs_played_none)
    lateinit var mBarChartTvNone: TextView

    @BindView(R.id.profile_view_under_bar_chart)
    lateinit var mViewBarChart: View

    @BindView(R.id.profile_chart_top_friends_title)
    lateinit var mTopFriendsTitle: TextView

    @Inject
    lateinit var mPresenter: IProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context!!).getAppComponent()?.plus(ProfileModule())?.inject(this)
        mPresenter.attachView(this)
        EventBus.getDefault().register(this)
        mPresenter.getProfile(forceNetworkRefresh = true)
        mSwipeRefresh.setOnRefreshListener { mPresenter.getProfile(forceNetworkRefresh = true) }
        displayBarChart()
        displayPieChart()
        return mView
    }

    /**
     * Display the bar chart (time played for each game)
     */
    private fun displayBarChart() {
        mChart.description.isEnabled = false
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
        mCurrentUser = user
        if (mCurrentUser!!.avatar != null) {
            Glide.with(context).load(mCurrentUser?.avatar).apply(RequestOptions()
                    .signature(ObjectKey(System.currentTimeMillis()))
                    .circleCrop()
                    .placeholder(R.drawable.ic_placeholder_circle_blue))
                    .into(mProfileAvatar)
        }
        mProfileName.text = context!!.getString(R.string.placeholder_name, mCurrentUser?.firstName, mCurrentUser?.lastName)
        if (!TextUtils.equals(user.firstName + " " + user.lastName, user.username)) {
            mProfileUsername.text = mCurrentUser?.username
        } else {
            mProfileUsername.visibility = View.GONE
        }
        showStats()
    }

    private fun showStats() {
        // BAR CHART
        if (mCurrentUser?.stats != null && mCurrentUser?.stats?.topSongs != null
                && mCurrentUser?.stats?.topSongs?.isNotEmpty()!!) {
            setBarChartData(mCurrentUser?.stats?.topSongs!!)
            mChart.visibility = View.VISIBLE
            mBarChartTvNone.visibility = View.GONE
        } else {
            mChart.visibility = View.INVISIBLE
            mBarChartTvNone.visibility = View.VISIBLE
        }
        // PIE CHART
        if (mCurrentUser?.stats != null && mCurrentUser?.stats?.topFriends != null
                && mCurrentUser?.stats?.topFriends?.isNotEmpty()!!) {
            setPieChartData(mCurrentUser?.stats?.topFriends!!)
            mPieChart.visibility = View.VISIBLE
        } else {
            mViewBarChart.visibility = View.GONE
            mPieChart.visibility = View.GONE
            mTopFriendsTitle.visibility = View.GONE
        }
    }

    /**
     * Add data to the bar chart
     */
    private fun setBarChartData(songs: List<Stats.Song>) {
        val songList = ArrayList<String>()
        val yVal = ArrayList<BarEntry>()
        songs.mapIndexed { index, song ->
            val value = song.nbUsed
            yVal.add(BarEntry(index.toFloat(), value!!.toFloat()))
            songList.add(song.name!!)
        }
        val set = BarDataSet(yVal, "Songs")
        val colors: MutableList<Int> = ArrayList()
        ColorTemplate.VORDIPLOM_COLORS.forEach { colors.add(it) }
        set.colors = colors
        set.setDrawValues(false)
        val data = BarData(set)
        mChart.data = data
        mChart.xAxis.valueFormatter = IndexAxisValueFormatter(songList)
        mChart.xAxis.labelCount = songs.size
        mChart.invalidate()
        mChart.animateY(500)
    }

    /**
     * Add data to the pie chart
     */
    private fun setPieChartData(topFriends: List<Stats.Friend>) {
        val yVal = ArrayList<PieEntry>()
        topFriends.map {
            yVal.add(PieEntry(it.nbNotifSent?.toFloat()!!, it.username))
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

    @OnClick(R.id.profile_avatar)
    fun editAvatar() {
        openBottomSheetDialog()
    }

    @OnClick(R.id.toolbar_settings)
    fun settingsClicked() {
        startActivity(SettingsActivity.getStartingIntent(context!!))
    }

    @OnClick(R.id.toolbar_achievements)
    fun achievementsClicked() {
        Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
        mSwipeRefresh.isRefreshing = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWriteStoragePermGrantedEvent(event: WriteStoragePermResultEvent) {
        if (event.isGranted) openLibrary() else displaySnackBar()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCameraPermsResultEvent(event: CameraPermsResultEvent) {
        if (event.isGranted) openCamera() else displaySnackBar()
    }

    /**
     * This method brings up the Bottom Sheet Dialog for picture Selection.
     */
    private fun openBottomSheetDialog() {
        mBottomSheetDialog = BottomSheetDialog(context!!)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_edit_profile, null)
        val btnAddFromCamera = view.findViewById<Button>(R.id.dialog_bottom_edit_profile_camera)
        val btnAddFromLibrary = view.findViewById<Button>(R.id.dialog_bottom_edit_profile_library)
        val btnCancel = view.findViewById<Button>(R.id.dialog_bottom_edit_profile_cancel)
        mBottomSheetDialog!!.setContentView(view)
        mBottomSheetDialog!!.show()
        btnAddFromCamera.setOnClickListener({
            openCamera()
            mBottomSheetDialog!!.dismiss()
        })
        btnAddFromLibrary.setOnClickListener({
            openLibrary()
            mBottomSheetDialog!!.dismiss()
        })
        btnCancel.setOnClickListener({ mBottomSheetDialog!!.dismiss() })
    }

    /**
     * Open the library of the phone
     */
    private fun openLibrary() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.REQUEST_PERMISSION_WRITE_STORAGE)
        } else {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, ACTIVITY_RESULT_LIBRARY)
        }
    }

    /**
     * Open the native camera of the phone
     */
    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            mCameraFile = mPresenter.setUpPhotoFile()
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile))
            if (Build.VERSION.SDK_INT >= 24) {
                startActivityForResult(Intent.createChooser(takePictureIntent, ""), ACTIVITY_RESULT_CAMERA)
            } else {
                startActivityForResult(takePictureIntent, ACTIVITY_RESULT_CAMERA)
            }
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.REQUEST_PERMISSION_GROUP_CAMERA)
        }
    }

    /**
     * Display SnackBar if permissions is denied.
     */
    private fun displaySnackBar() {
        showSnackbar(R.string.permission_denied_explanation, R.string.settings, View.OnClickListener {
            // Build intent that displays the App settings screen.
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })
    }

    /**
     * Shows a [Snackbar].
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_LONG)
                .setAction(getString(actionStringId), listener).show()
    }

    /**
     * Compress the picture.
     *
     * @param bitmap Bitmap
     * @return File
     * @throws IOException IOException
     */
    @Throws(IOException::class)
    private fun compressPicture(bitmap: Bitmap): File {
        //create a file to write bitmap data
        val f = File(context!!.cacheDir, "tmpPicture")
        f.createNewFile()
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos)
        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        return f
    }

    /**
     * Get the real path of an URI
     *
     * @param contentURI Uri
     * @return path String?
     */
    private fun getRealPathFromURI(contentURI: Uri): String? {
        var result: String? = null
        val cursor = context!!.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
            }
            cursor.close()
        }
        return result
    }

    /**
     * CallBack library
     *
     * @param requestCode int
     * @param resultCode  int
     * @param data        Intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_RESULT_LIBRARY) {
            val bitmap = BitmapFactory.decodeFile(getRealPathFromURI(data!!.data))
            val resized = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
            mPresenter.updateProfilePicture(compressPicture(resized))
        }
        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_RESULT_CAMERA) {
            val bitmap = BitmapFactory.decodeFile(mCameraFile?.path)
            val resized = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
            mPresenter.updateProfilePicture(compressPicture(resized))
        }
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        mPresenter.detachView()
        super.onDestroyView()
    }
}
