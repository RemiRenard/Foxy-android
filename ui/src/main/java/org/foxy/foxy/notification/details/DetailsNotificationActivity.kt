package org.foxy.foxy.notification.details

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.data.Constants
import org.foxy.data.model.Notification
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.notification.dagger.NotificationModule
import javax.inject.Inject
import android.media.MediaPlayer.OnCompletionListener


class DetailsNotificationActivity : BaseActivity(), IDetailsNotificationView {

    private var mNotification: Notification? = null
    private var mPlayer: MediaPlayer? = null
    private var mIsPlaying: Boolean = false

    @BindView(R.id.details_notification_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.toolbar_title)
    lateinit var mToolBarTitle: TextView

    @BindView(R.id.details_notification_content)
    lateinit var mContentNotif: TextView

    @BindView(R.id.details_notification_play_audio)
    lateinit var mPlayAudio: ImageView

    @BindView(R.id.details_notification_play_text)
    lateinit var mTextPlay: TextView

    @BindView(R.id.details_notification_audio_text)
    lateinit var mTextAudio: TextView

    @Inject
    lateinit var mPresenter: IDetailsNotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_notification)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(NotificationModule())?.inject(this)
        ButterKnife.bind(this)
        mPresenter.attachView(this)
        mNotification = intent.getSerializableExtra(Constants.EXTRA_NOTIFICATION) as Notification?
        //If no song -> hide play button
        if (TextUtils.equals(mNotification?.song, "default_song_location")) mPlayAudio.visibility = View.GONE
        mToolBarTitle.text = mNotification?.title
        mToolBarTitle.isSelected = true
        mContentNotif.text = mNotification?.content
        mContentNotif.movementMethod = ScrollingMovementMethod()
    }

    /**
     * Play if not playing
     * Stop if playing
     */
    @OnClick(R.id.details_notification_play_audio)
    fun playAudioClicked() {
        if (!mIsPlaying) {
            startPlaying()
        } else {
            stopPlaying()
        }

    }

    private fun startPlaying() {
        mIsPlaying = true
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(this, Uri.parse(mNotification?.song))
        mPlayer?.setOnCompletionListener {
            mIsPlaying = false
            audioButtonChange()
        }
        mPlayer?.prepare()
        mPlayer?.start()
        audioButtonChange()
    }

    private fun stopPlaying() {
        mPlayer?.release()
        mPlayer = null
        mIsPlaying = false
        audioButtonChange()
    }


    /**
     * Changing play button color and 'play' text to stop
     */
    private fun audioButtonChange() {
        if (mIsPlaying) {
            mPlayAudio.setImageResource(R.drawable.ic_speaker_off)
            mTextAudio.setTextColor(resources.getColor(R.color.colorRed))
            mTextPlay.setTextColor(resources.getColor(R.color.colorRed))
            mTextPlay.setText(R.string.stop)
        } else {
            mPlayAudio.setImageResource(R.drawable.ic_speaker_on)
            mTextAudio.setTextColor(resources.getColor(R.color.colorPrimary))
            mTextPlay.setTextColor(resources.getColor(R.color.colorPrimary))
            mTextPlay.setText(R.string.play)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @param notification  Notification
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context, notification: Notification): Intent {
            val intent = Intent(callingContext, DetailsNotificationActivity::class.java)
            intent.putExtra(Constants.EXTRA_NOTIFICATION, notification)
            return intent
        }
    }
}
