package com.app.foxy.notification.recordVoice

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
import android.util.Log
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.data.Constants
import com.app.foxy.BaseActivity
import com.app.foxy.BuildConfig
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.custom.ButtonRecord
import com.app.foxy.notification.dagger.NotificationModule
import com.app.foxy.notification.selectFriends.SelectFriendsActivity
import java.io.File
import javax.inject.Inject


/**
 * RecordVoiceActivity class.
 */
class RecordVoiceActivity : BaseActivity(), IRecordVoiceView, IButtonRecord {

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mAudioFileName: String? = null
    private var mAudioFile: File? = null
    private var mIsRecording: Boolean = false
    private var mIsPlaying: Boolean = false
    private var mIsFileRecorded = false

    @BindView(R.id.record_voice_button)
    lateinit var mButtonRecord: ButtonRecord

    @BindView(R.id.record_voice_message)
    lateinit var mMessage: EditText

    @Inject
    lateinit var mPresenter: IRecordVoicePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_voice)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(NotificationModule())?.inject(this)
        mPresenter.attachView(this)
        mButtonRecord.setListener(this)
        mAudioFileName = externalCacheDir.absolutePath + "/foxyAudioRecord.mp3"
        mAudioFile = File(mAudioFileName)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    @OnClick(R.id.record_voice_back)
    fun back() {
        onBackPressed()
    }

    /**
     * Callback from ButtonRecord : start
     */
    override fun startRecord() {
        startRecordVoice()
    }

    /**
     * Callback from ButtonRecord : stop
     */
    override fun stopRecord() {
        stopRecordVoice()
    }

    override fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_PERMISSIONS_AUDIO_RECORD)
    }

    override fun showProgressBar() {
        // Do nothing.
    }

    override fun hideProgressBar() {
        // Do nothing.
    }

    override fun openFriendsActivity() {
        startActivity(SelectFriendsActivity.getStartingIntent(this))
        finish()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    /**
     * Start the recording.
     */
    private fun startRecordVoice() {
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setOutputFile(mAudioFileName)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder?.setMaxDuration(Constants.RECORD_MAX_DURATION)
        mRecorder?.prepare()
        mRecorder?.start()
        mIsRecording = true
    }

    /**
     * Stop the recording.
     */
    private fun stopRecordVoice() {
        try {
            mIsFileRecorded = true
            mRecorder?.stop()
            mRecorder?.release()
            mRecorder = null
            mIsRecording = false
            mPresenter.saveTmpNotification(mMessage.text.toString(), mAudioFile)
        } catch (stopException: RuntimeException) {
            Log.e(this.javaClass.simpleName, stopException.message)
        }
    }

    /**
     * Unused for now.
     */
    private fun startPlaying() {
        mIsPlaying = true
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(mAudioFileName)
        mPlayer?.prepare()
        mPlayer?.start()
        //mPlayer?.setOnCompletionListener { stopPlaying() }
    }

    /**
     * Unused for now.
     */
    private fun stopPlaying() {
        mPlayer?.release()
        mPlayer = null
        mIsPlaying = false
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

    public override fun onStop() {
        super.onStop()
        mRecorder?.release()
        mRecorder = null
        mPlayer?.release()
        mPlayer = null
    }

    companion object {

        const val REQUEST_PERMISSIONS_AUDIO_RECORD = 12

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent =
                Intent(callingContext, RecordVoiceActivity::class.java)
    }
}
