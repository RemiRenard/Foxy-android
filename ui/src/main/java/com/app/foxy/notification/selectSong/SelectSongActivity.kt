package com.app.foxy.notification.selectSong

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
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.data.Constants
import com.app.data.model.Song
import com.app.foxy.BaseActivity
import com.app.foxy.BuildConfig
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.eventBus.SongSelectedNotifEvent
import com.app.foxy.notification.dagger.NotificationModule
import com.app.foxy.notification.selectFriends.SelectFriendsActivity
import com.app.foxy.notification.selectSong.adapter.SongAdapter
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import javax.inject.Inject

/**
 * SelectSongActivity class.
 */
class SelectSongActivity : BaseActivity(), ISelectSongView {

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mAudioFileName: String? = null
    private var mAudioFile: File? = null
    private var mIsRecording: Boolean = false
    private var mIsPlaying: Boolean = false
    private var mIsFileRecorded = false

    @BindView(R.id.select_sound_recycler_view)
    lateinit var mRecyclerView: RecyclerView

    @Inject
    lateinit var mPresenter: ISelectSongPresenter

    @Inject
    lateinit var mSongAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sound)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(NotificationModule())?.inject(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(this, 2)
        // mRecyclerView.addItemDecoration(SpacesItemDecoration(1))
        mRecyclerView.adapter = mSongAdapter
        mPresenter.attachView(this)
        mPresenter.getSongs(true)
        mAudioFileName = externalCacheDir.absolutePath + "/foxyAudioRecord.mp3"
        mAudioFile = File(mAudioFileName)
        mPresenter.manageTutorial()
    }

    override fun onResume() {
        super.onResume()
        mSongAdapter.onResume()
    }

    override fun onPause() {
        mSongAdapter.onPause()
        super.onPause()
    }

    override fun showTutorial() {
        TapTargetSequence(this).targets(
                TapTarget.forView(mRecyclerView,
                        getString(R.string.tuto_list_songs),
                        getString(R.string.tuto_list_songs_desc))
                        .outerCircleColor(R.color.colorPrimary)
                        .textColor(android.R.color.white)
                        .transparentTarget(true)
                        .targetCircleColor(android.R.color.white)
        ).listener(object : TapTargetSequence.Listener {
            override fun onSequenceCanceled(lastTarget: TapTarget?) {
                // Do nothing.
            }

            override fun onSequenceFinish() {
                // Do nothing.
            }

            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                // Do nothing.
            }
        }).continueOnCancel(true).start()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSongSelectedNotifUpdateEvent(event: SongSelectedNotifEvent) {
        mPresenter.saveTmpNotification(event.song.id!!)
    }

    fun playAudioClicked() {
        if (!mIsPlaying) {
            startPlaying()
        } else {
            stopPlaying()
        }
    }

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
        //mMicro.setImageResource(R.drawable.ic_micro_recording)
        //mPlayAudio.visibility = View.GONE
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
        mIsRecording = true
    }

    private fun stopRecordVoice() {
        mIsFileRecorded = true
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
        mIsRecording = false
        //mPlayAudio.visibility = View.VISIBLE
        //mMicro.setImageResource(R.drawable.ic_micro_not_recording)
    }

    private fun startPlaying() {
        mIsPlaying = true
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(mAudioFileName)
        mPlayer?.prepare()
        mPlayer?.start()
        mPlayer?.setOnCompletionListener { stopPlaying() }
        //mPlayAudio.setImageResource(R.drawable.ic_speaker_off)
    }

    private fun stopPlaying() {
        mPlayer?.release()
        mPlayer = null
        mIsPlaying = false
        //mPlayAudio.setImageResource(R.drawable.ic_speaker_on)
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
                Intent(callingContext, SelectSongActivity::class.java)
    }
}
