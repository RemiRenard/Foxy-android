package com.app.foxy.update

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.foxy.BaseActivity
import com.app.foxy.R
import com.thefinestartist.finestwebview.FinestWebView

class ForceUpdateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_force_update)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.force_update_button)
    fun upgradeClicked() {
        val uri = Uri.parse("market://details?id=" + this.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
            finish()
        } catch (e: ActivityNotFoundException) {
            FinestWebView.Builder(this)
                    .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                            R.anim.activity_close_enter, R.anim.activity_close_exit)
                    .show("http://play.google.com/store/apps/details?id=" + this.packageName)
        }
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent = Intent(callingContext,
                ForceUpdateActivity::class.java)
    }
}
