package org.foxy.foxy.play.findit.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.data.Constants
import org.foxy.data.model.Game
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.play.findit.dagger.FinditModule
import org.foxy.foxy.play.findit.lobby.FinditLobbyActivity
import javax.inject.Inject

class FinditHomeActivity : BaseActivity(), IFinditHomeView {

    private var mGame: Game? = null

    @BindView(R.id.findit_home_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.toolbar_title)
    lateinit var mToolbarTitle: TextView

    @Inject
    lateinit var mPresenter: IFinditHomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findit_home)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(FinditModule())?.inject(this)
        ButterKnife.bind(this)
        mGame = intent.getSerializableExtra(Constants.EXTRA_GAME) as Game?
        mToolbarTitle.text = mGame?.title
        mPresenter.attachView(this)
    }

    @OnClick(R.id.toolbar_cancel)
    fun cancel() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    @OnClick(R.id.findit_home_create_game)
    fun createGame() {
        mPresenter.connectWebSocket()
        startActivity(FinditLobbyActivity.getStartingIntent(this))
    }

    @OnClick(R.id.findit_home_join_game)
    fun joinGame() {
        mPresenter.connectWebSocket()
        startActivity(FinditLobbyActivity.getStartingIntent(this))
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @param game  Game
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context, game: Game?): Intent {
            val intent = Intent(callingContext, FinditHomeActivity::class.java)
            intent.putExtra(Constants.EXTRA_GAME, game)
            return intent
        }
    }
}
