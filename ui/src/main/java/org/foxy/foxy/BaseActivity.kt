package org.foxy.foxy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.foxy.data.Constants
import org.foxy.domain.Domain
import org.foxy.domain.event_bus.NetworkErrorEvent
import org.foxy.domain.event_bus.NetworkStateChangeEvent
import org.foxy.foxy.connect.ConnectActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * The basic activity for an activity. All activities should extend it.
 */
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
