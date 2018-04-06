package com.app.foxy.notification.selectSong

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.data.model.Song
import com.app.foxy.BaseActivity
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
import javax.inject.Inject

/**
 * SelectSongActivity class.
 */
class SelectSongActivity : BaseActivity(), ISelectSongView {

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
        mRecyclerView.adapter = mSongAdapter
        mPresenter.attachView(this)
        mPresenter.getSongs(true)
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

    override fun openFriendsActivity() {
        startActivity(SelectFriendsActivity.getStartingIntent(this))
        finish()
    }

    override fun displaySongs(songs: List<Song>) {
        mSongAdapter.setData(songs)
    }

    override fun showProgressBar() {
        // Do nothing.
    }

    override fun hideProgressBar() {
        // Do nothing.
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent =
                Intent(callingContext, SelectSongActivity::class.java)
    }
}
