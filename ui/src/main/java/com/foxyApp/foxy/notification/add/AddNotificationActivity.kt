package com.foxyApp.foxy.notification.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.foxyApp.data.Constants
import com.foxyApp.data.model.Song
import com.foxyApp.foxy.BaseActivity
import com.foxyApp.foxy.BuildConfig
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.adapter.SongAdapter
import com.foxyApp.foxy.custom.SimpleDividerItemDecoration
import com.foxyApp.foxy.eventBus.SongSelectedNotifEvent
import com.foxyApp.foxy.notification.dagger.NotificationModule
import com.foxyApp.foxy.notification.selectFriends.SelectFriendsActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import javax.inject.Inject

/**
 * AddNotificationActivity class.
 */
class AddNotificationActivity : BaseActivity(), IAddNotificationView {

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mAudioFileName: String? = null
    private var mAudioFile: File? = null
    private var mIsRecording: Boolean = false
    private var mIsPlaying: Boolean = false
    private var mIsFileRecorded = false
    private var mSongIdSelected: String = ""

    @BindView(R.id.add_notification_message)
    lateinit var mMessage: EditText

    @BindView(R.id.add_notification_micro)
    lateinit var mMicro: ImageView

    @BindView(R.id.add_notification_play_audio)
    lateinit var mPlayAudio: ImageView

    @BindView(R.id.add_notification_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.add_notification_button_next)
    lateinit var mButtonNext: Button

    @Inject
    lateinit var mPresenter: IAddNotificationPresenter

    @Inject
    lateinit var mSongAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notification)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(NotificationModule())?.inject(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        mRecyclerView.adapter = mSongAdapter
        mPresenter.attachView(this)
        mPresenter.getSongs(true)
        mAudioFileName = externalCacheDir.absolutePath + "/foxyAudioRecord.mp3"
        mAudioFile = File(mAudioFileName)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSongSelectedNotifUpdateEvent(event: SongSelectedNotifEvent) {
        if (event.song.id != null) {
            mSongIdSelected = event.song.id!!
            mButtonNext.visibility = View.VISIBLE
        } else if (!mIsFileRecorded) {
            mSongIdSelected = ""
            mButtonNext.visibility = View.GONE
        }
    }

    @OnClick(R.id.toolbar_back)
    fun back(){
        onBackPressed()
    }

    @OnClick(R.id.add_notification_button_next)
    fun sendNotification() {
        mPresenter.saveTmpNotification(mMessage.text.toString(), mSongIdSelected, "message", mAudioFile)
    }

    @OnClick(R.id.add_notification_play_audio)
    fun playAudioClicked() {
        if (!mIsPlaying) {
            startPlaying()
        } else {
            stopPlaying()
        }
    }

    @OnClick(R.id.add_notification_micro)
    fun startRecordClicked() {
        if (mIsPlaying)
            stopPlaying()
        if (mIsRecording) {
            stopRecordVoice()
        } else {
            if (checkPermissions()) {
                startRecordVoice()
            } else {
                requestPermissions()
            }
        }
    }

    override fun openFriendsActivity() {
        startActivity(SelectFriendsActivity.getStartingIntent(this))
        finish()
    }

    override fun displaySongs(songs: List<Song>) {
        mSongAdapter.setData(songs)
    }

    private fun startRecordVoice() {
        mPlayAudio.visibility = View.GONE
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setOutputFile(mAudioFileName)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder?.setMaxDuration(Constants.RECORD_MAX_DURATION)
        mRecorder?.setOnInfoListener { _, what, _ ->
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                stopRecordVoice()
            }
        }
        mRecorder?.prepare()
        mRecorder?.start()
        mMicro.setImageResource(R.drawable.ic_micro_recording)
        mIsRecording = true
    }

    private fun stopRecordVoice() {
        mButtonNext.visibility = View.VISIBLE
        mIsFileRecorded = true
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
        mIsRecording = false
        mMicro.setImageResource(R.drawable.ic_micro_not_recording)
        mPlayAudio.visibility = View.VISIBLE
    }

    private fun startPlaying() {
        mIsPlaying = true
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(mAudioFileName)
        mPlayer?.prepare()
        mPlayer?.start()
        mPlayer?.setOnCompletionListener { stopPlaying() }
        mPlayAudio.setImageResource(R.drawable.ic_speaker_off)
    }

    private fun stopPlaying() {
        mPlayer?.release()
        mPlayer = null
        mIsPlaying = false
        mPlayAudio.setImageResource(R.drawable.ic_speaker_on)
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request permissions.
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSIONS_AUDIO_RECORD)
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS_AUDIO_RECORD) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecordVoice()
            } else {
                displaySnackBar()
            }
        }
    }

    /**
     * Display SnackBar if permissions is denied.
     */
    private fun displaySnackBar() {
        showSnackbar(R.string.permission_denied_explanation, R.string.Settings,
                {
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
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int, listener: (Any) -> Unit) {
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId),
                Snackbar.LENGTH_LONG).setAction(getString(actionStringId), listener).show()
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

    }

    public override fun onStop() {
        super.onStop()
        mRecorder?.release()
        mRecorder = null
        mPlayer?.release()
        mPlayer = null
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    companion object {

        const val REQUEST_PERMISSIONS_AUDIO_RECORD = 12

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent =
                Intent(callingContext, AddNotificationActivity::class.java)
    }
}
