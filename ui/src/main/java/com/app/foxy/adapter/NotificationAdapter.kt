package com.app.foxy.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_notification.view.*
import com.app.data.Constants
import com.app.data.model.Notification
import com.app.foxy.R
import com.app.foxy.eventBus.NotificationClickedEvent
import com.app.foxy.friends.requests.FriendsRequestsActivity
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter used to display notifications.
 */
class NotificationAdapter(val mContext: Context) : RecyclerView.Adapter<NotificationAdapter.ItemViewHolder>() {

    private var mNotifications: MutableList<Notification> = ArrayList()
    private var mPlayer: MediaPlayer? = null
    private var mIsPlaying: Boolean = false
    private var mPositionPlaying: Int = -1
    private val mDatePattern: String = "HH:mm"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_notification_message?.text = mNotifications[position].message
        holder.itemView?.item_notification_user_time?.text = mContext.getString(
                R.string.user_time,
                mNotifications[position].userSource?.username,
                SimpleDateFormat(mDatePattern, Locale.US).format(mNotifications[position].createdAt))
        holder.itemView?.item_notification_layout?.setOnClickListener {
            if (!mIsPlaying) {
                // Mark notification as read.
                EventBus.getDefault().post(NotificationClickedEvent(mNotifications[position].id!!))
                mNotifications[position].isRead = true
                notifyDataSetChanged()
            }
            // Manage the type of notification
            if (mNotifications[position].type.equals("friendRequest")) {
                mContext.startActivity(FriendsRequestsActivity.getStartingIntent(mContext)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        if (mNotifications[position].isRead)
            holder.itemView?.item_notification_layout?.alpha = 0.8F
        else
            holder.itemView?.item_notification_layout?.alpha = 1F

        if (TextUtils.equals(mNotifications[position].song, Constants.DEFAULT_SONG_LOCATION)
                || TextUtils.equals(mNotifications[position].song, ""))
            holder.itemView?.item_notification_audio_button?.visibility = View.GONE
        else
            holder.itemView?.item_notification_audio_button?.visibility = View.VISIBLE
        // When the use click on "play"
        holder.itemView?.item_notification_audio_button?.setOnClickListener {
            if (!mIsPlaying) {
                mIsPlaying = true
                mPositionPlaying = position
                //If nothing is currently played
                holder.itemView.item_notification_progress_bar.visibility = View.VISIBLE
                holder.itemView.item_notification_audio_button.visibility = View.GONE
                mPlayer = MediaPlayer()
                try {
                    mPlayer?.setDataSource(mContext, Uri.parse(mNotifications[position].song))
                    mPlayer?.prepareAsync()
                    mPlayer?.setOnPreparedListener {
                        holder.itemView.item_notification_progress_bar.visibility = View.GONE
                        holder.itemView.item_notification_audio_button.visibility = View.VISIBLE
                        holder.itemView.item_notification_audio_button.setImageResource(R.drawable.ic_stop)
                        mPlayer?.start()
                    }
                    mPlayer?.setOnCompletionListener {
                        mPlayer = null
                        holder.itemView.item_notification_audio_button.setImageResource(R.drawable.ic_play)
                        mIsPlaying = false
                        mPositionPlaying = -1
                        // Mark notification as read.
                        EventBus.getDefault().post(NotificationClickedEvent(mNotifications[position].id!!))
                        mNotifications[position].isRead = true
                        notifyDataSetChanged()
                    }
                } catch (e: IOException) {
                    Log.e(javaClass.simpleName, e.message)
                } catch (e: IllegalStateException) {
                    Log.e(javaClass.simpleName, e.message)
                }
            } else { //If an other notification's audio is currently played
                if (mPositionPlaying == position) {
                    mPlayer?.release()
                    mIsPlaying = false
                    mPositionPlaying = -1
                    holder.itemView.item_notification_audio_button.setImageResource(R.drawable.ic_play)
                } else {
                    Toast.makeText(mContext, R.string.stop_audio_first, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = mNotifications.size

    /**
     * Put data in the recycler view.
     */
    fun setData(notifications: List<Notification>) {
        mNotifications.clear()
        mNotifications.addAll(notifications)
        notifyDataSetChanged()
    }

    /**
     * View holder of the notification item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
