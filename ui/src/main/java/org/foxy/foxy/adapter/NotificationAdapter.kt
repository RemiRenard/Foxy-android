package org.foxy.foxy.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_notification.view.*
import org.foxy.data.model.Notification
import org.foxy.foxy.R
import org.foxy.foxy.event_bus.NotificationClickedEvent
import org.foxy.foxy.notification.details.DetailsNotificationActivity
import org.foxy.foxy.profile.friends.requests.FriendsRequestsActivity
import org.greenrobot.eventbus.EventBus

/**
 * Adapter used to display notifications.
 */
class NotificationAdapter(val mContext: Context) : RecyclerView.Adapter<NotificationAdapter.ItemViewHolder>() {

    private var mNotifications: MutableList<Notification> = ArrayList()

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
            if (mNotifications[position].type.equals("message")) {
                mContext.startActivity(DetailsNotificationActivity.getStartingIntent(mContext,
                        mNotifications[position]).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            if (mNotifications[position].type.equals("friendRequest")) {
                mContext.startActivity(FriendsRequestsActivity.getStartingIntent(mContext)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
        if (mNotifications[position].isRead) {
            holder.itemView?.item_notification_layout?.alpha = 0.8F
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
