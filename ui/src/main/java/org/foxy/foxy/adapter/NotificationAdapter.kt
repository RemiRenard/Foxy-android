package org.foxy.foxy.adapter

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
import org.foxy.data.model.Notification
import org.foxy.foxy.R
import org.foxy.foxy.event_bus.NotificationClickedEvent
import org.foxy.foxy.profile.friends.requests.FriendsRequestsActivity
import org.greenrobot.eventbus.EventBus

/**
 * Adapter used to display notifications.
 */
class NotificationAdapter(val mContext: Context) : RecyclerView.Adapter<NotificationAdapter.ItemViewHolder>() {

    private var mNotifications: MutableList<Notification> = ArrayList()
    private var mPlayer: MediaPlayer? = null
    private var mIsPlaying: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_notification_title?.text = mNotifications[position].title
        holder.itemView?.item_notification_content?.text = mNotifications[position].content
        holder.itemView?.item_notification_layout?.setOnClickListener {
            // Mark notification as read.
            EventBus.getDefault().post(NotificationClickedEvent(mNotifications[position].id!!))
            mNotifications[position].isRead = true
            notifyDataSetChanged()
            // Manage the type of notification
            if (mNotifications[position].type.equals("friendRequest")) {
                mContext.startActivity(FriendsRequestsActivity.getStartingIntent(mContext)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
        if (mNotifications[position].isRead) {
            holder.itemView?.item_notification_layout?.alpha = 0.7F
        }

        if (TextUtils.equals(mNotifications[position].song, "default_song_location")
                || TextUtils.equals(mNotifications[position].song, ""))
            holder.itemView?.item_notification_audio_button?.visibility = View.GONE
        else
            holder.itemView?.item_notification_audio_button?.visibility = View.VISIBLE

        holder.itemView?.item_notification_audio_button?.setOnClickListener {
            Log.i("OnclickListener BEGIN", mIsPlaying.toString())
            if (mIsPlaying==-1) {
                mIsPlaying=position
                mPlayer = MediaPlayer()
                mPlayer?.setDataSource(mContext, Uri.parse(mNotifications[position].song))
                mPlayer?.setOnCompletionListener {
                    mIsPlaying = -1
                    mPlayer = null
                    holder.itemView.item_notification_audio_button.setImageResource(R.drawable.ic_play)
                    Log.i("OnclickListener FINISH", mIsPlaying.toString())
                }
                mPlayer?.prepare()
                mPlayer?.start()
                holder.itemView.item_notification_audio_button.setImageResource(R.drawable.ic_stop)

                Log.i("OnclickListener PLAY", mIsPlaying.toString())
            } else if(mIsPlaying==position){
                mPlayer?.release()
                mPlayer = null
                mIsPlaying = -1
                holder.itemView.item_notification_audio_button.setImageResource(R.drawable.ic_play)

                Log.i("OnclickListener STOP", mIsPlaying.toString())
            } else {
                Toast.makeText(mContext, R.string.stop_audio_first, Toast.LENGTH_SHORT).show()
                Log.i("OnclickListener STUCK", mIsPlaying.toString())
            }
            Log.i("OnclickListener END", mIsPlaying.toString())
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
