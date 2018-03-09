package com.foxyApp.foxy.profile.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.thefinestartist.finestwebview.FinestWebView
import com.foxyApp.foxy.BaseActivity
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.connect.ConnectActivity
import com.foxyApp.foxy.profile.dagger.ProfileModule
import com.foxyApp.foxy.profile.edit.EditProfileActivity
import javax.inject.Inject


/**
 * SettingsActivity class.
 */
class SettingsActivity : BaseActivity(), ISettingsView {

    @BindView(R.id.settings_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.settings_logout_button)
    lateinit var mLogout: Button

    @Inject
    lateinit var mPresenter: ISettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(ProfileModule())?.inject(this)
        mPresenter.attachView(this)
    }

    @OnClick(R.id.settings_edit_profile)
    fun editProfile() {
        startActivity(EditProfileActivity.getStartingIntent(this))
    }

    @OnClick(R.id.toolbar_back)
    fun back() {
        onBackPressed()
    }

    @OnClick(R.id.settings_share_app)
    fun shareApp() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message,
                "http://play.google.com/store/apps/details?id=" + this.packageName))
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, ""))
    }

    @OnClick(R.id.settings_rate_app)
    fun rateApp() {
        val uri = Uri.parse("market://details?id=" + this.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            FinestWebView.Builder(this)
                    .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                            R.anim.activity_close_enter, R.anim.activity_close_exit)
                    .show("http://play.google.com/store/apps/details?id=" + this.packageName)
        }
    }

    @OnClick(R.id.settings_terms_and_conditions)
    fun termsAndConditions() {
        FinestWebView.Builder(this)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show("http://foxy-app.com/terms-and-conditions")
    }

    @OnClick(R.id.settings_privacy_policy)
    fun privacyPolicy() {
        FinestWebView.Builder(this)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show("http://foxy-app.com/privacy-policy")
    }

    @OnClick(R.id.settings_logout_button)
    fun logout() {
        mPresenter.logout()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun enableLogoutButton(isEnabled: Boolean) {
        mLogout.isEnabled = isEnabled
    }

    override fun logoutComplete() {
        enableLogoutButton(true)
        hideProgressBar()
        val intent = ConnectActivity.getStartingIntent(this)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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
        fun getStartingIntent(callingContext: Context): Intent = Intent(callingContext,
                SettingsActivity::class.java)
    }
}
