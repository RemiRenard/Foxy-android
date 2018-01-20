package org.foxy.foxy.search.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.foxy.data.Constants
import org.foxy.data.model.Game
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.play.findit.home.FinditHomeActivity
import org.foxy.foxy.search.game.dagger.GameModule
import javax.inject.Inject

/**
 * GameActivity class.
 */
class GameActivity : BaseActivity(), IGameView {

    private var mGame: Game? = null

    @BindView(R.id.toolbar_title)
    lateinit var mToolBarTitle: TextView

    @BindView(R.id.game_description)
    lateinit var mDescription: TextView

    @BindView(R.id.game_rules)
    lateinit var mRules: TextView

    @BindView(R.id.game_requirements)
    lateinit var mRequirements: TextView

    @BindView(R.id.game_players)
    lateinit var mPlayers: TextView

    @BindView(R.id.game_picture)
    lateinit var mPicture: ImageView

    @Inject
    lateinit var mPresenter: IGamePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(GameModule())?.inject(this)
        mGame = intent.getSerializableExtra(Constants.EXTRA_GAME) as Game?
        mToolBarTitle.text = mGame?.title
        mDescription.text = mGame?.description
        mRules.text = mGame?.rules
        mRequirements.text = mGame?.requirements?.joinToString("\n", "", "")
        mPlayers.text = getString(R.string.placeholder_game_players, mGame?.nbMinPlayer, mGame?.nbMaxPlayer)
        Glide.with(this).load(mGame?.picture).apply(RequestOptions()
                .placeholder(R.drawable.ic_placeholder_square_gray))
                .into(mPicture)

        mPresenter.attachView(this)
    }

    @OnClick(R.id.toolbar_back)
    fun back() {
        onBackPressed()
    }

    @OnClick(R.id.game_button_play)
    fun play() {
        startActivity(FinditHomeActivity.getStartingIntent(this, mGame))
    }

    override fun showProgressBar() {
        // Do nothing
    }

    override fun hideProgressBar() {
        // Do nothing
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
        fun getStartingIntent(callingContext: Context, game: Game): Intent {
            val intent = Intent(callingContext, GameActivity::class.java)
            intent.putExtra(Constants.EXTRA_GAME, game)
            return intent
        }
    }
}
