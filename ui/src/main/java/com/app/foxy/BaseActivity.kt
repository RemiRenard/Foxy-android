package com.app.foxy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.app.data.Constants
import com.app.domain.eventBus.NetworkErrorEvent
import com.app.domain.eventBus.NetworkStateChangeEvent
import com.app.foxy.connect.ConnectActivity
import com.app.foxy.eventBus.DisplaySnackBarEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * The basic activity for an activity. All activities should extend it.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.inject(this)
    }

    override fun onStart() {
        super.onStart()
        // Register to eventBus.
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkStateChangeEvent(event: NetworkStateChangeEvent) {
        if (!event.isInternet) {
            alertDialog().show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkErrorEvent(event: NetworkErrorEvent) {
        when (event.code) {
            Constants.SESSION_EXPIRED -> {
                Toast.makeText(this,
                        R.string.session_expired_message, Toast.LENGTH_SHORT).show()
                val intent = ConnectActivity.getStartingIntent(this)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDisplaySnackBarEvent(event: DisplaySnackBarEvent) {
        displaySnackBar()
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


    protected fun alertDialog(): AlertDialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.No_internet_connection))
                .setPositiveButton(getString(R.string.Ok)) { _, _ -> builder.create().dismiss() }
        // Create the AlertDialog object and return it
        return builder.create()
    }

    override fun onStop() {
        // Unregister to eventBus.
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
