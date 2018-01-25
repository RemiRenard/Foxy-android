package org.foxy.foxy.notification.add

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
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.BuildConfig
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.notification.dagger.NotificationModule
import org.foxy.foxy.notification.select_friends.SelectFriendsActivity
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

    @BindView(R.id.add_notification_message)
    lateinit var mMessage: EditText

    @BindView(R.id.add_notification_keyword)
    lateinit var mKeyword: EditText

    @BindView(R.id.add_notification_micro)
    lateinit var mMicro: ImageView

    @BindView(R.id.add_notification_play_audio)
    lateinit var mPlayAudio: ImageView

    @BindView(R.id.add_notification_keyword_text)
    lateinit var mKeywordText: TextView

    @Inject
    lateinit var mPresenter: IAddNotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notification)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(NotificationModule())?.inject(this)
        mPresenter.attachView(this)
        mAudioFileName = externalCacheDir.absolutePath + "/foxyAudioRecord.mp3"
        mAudioFile = File(mAudioFileName)
    }

    @OnClick(R.id.add_notification_button_next)
    fun sendNotification() {
        if (!mMessage.text.isEmpty() && !mKeyword.text.isEmpty()) {
            mPresenter.saveTmpNotification(mMessage.text.toString(), mKeyword.text.toString(), "message", "", mAudioFile)
        } else {
            Toast.makeText(this, R.string.error_form, Toast.LENGTH_SHORT).show()
        }
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
    fun startAudioClicked() {
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

    private fun startRecordVoice() {
        mPlayAudio.visibility = View.GONE
        mKeywordText.visibility = View.GONE
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setOutputFile(mAudioFileName)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder?.prepare()
        mRecorder?.start()
        mMicro.setImageResource(R.drawable.ic_micro_recording)
        mIsRecording = true
    }

    private fun stopRecordVoice() {
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
        mIsRecording = false
        mMicro.setImageResource(R.drawable.ic_micro_not_recording)
        mPlayAudio.visibility = View.VISIBLE;
    }

    private fun startPlaying() {
        mIsPlaying = true
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(mAudioFileName)
        mPlayer?.prepare()
        mPlayer?.start()
        mPlayer?.setOnCompletionListener { mIsPlaying = false }
    }

    private fun stopPlaying() {
        mPlayer?.release()
        mPlayer = null
        mIsPlaying = false
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
