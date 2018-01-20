package org.foxy.foxy.play.findit.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.play.findit.dagger.FinditModule
import javax.inject.Inject

class FinditLobbyActivity : BaseActivity(), IFinditLobbyView {

    @BindView(R.id.findit_lobby_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @Inject
    lateinit var mPresenter: IFinditLobbyPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findit_lobby)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(FinditModule())?.inject(this)
        ButterKnife.bind(this)
        mPresenter.attachView(this)
    }

    @OnClick(R.id.toolbar_cancel)
    fun cancel() {
        onBackPressed()
        mPresenter.disconnectWebSocket()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        mPresenter.disconnectWebSocket()
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
                Intent(callingContext, FinditLobbyActivity::class.java)
    }
}
