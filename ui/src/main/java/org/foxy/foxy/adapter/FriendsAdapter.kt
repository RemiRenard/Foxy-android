package org.foxy.foxy.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.thefinestartist.utils.content.ContextUtil.startActivity
import kotlinx.android.synthetic.main.item_friends.view.*
import org.foxy.data.model.User
import org.foxy.foxy.R
import org.foxy.foxy.notification.add.AddNotificationActivity
import java.util.*

/**
 * Adapter used to display friends.
 */
class FriendsAdapter(val mContext: Context) : RecyclerView.Adapter<FriendsAdapter.ItemViewHolder>() {

    private var mFriends: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friends, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_friends_name?.text = mFriends[position].username
        if (mFriends[position].avatar.isNullOrEmpty()) {
            holder.itemView?.item_friends_avatar?.setImageResource(R.drawable.ic_placeholder_male)
        } else {
            Glide.with(mContext).load(mFriends[position].avatar).into(holder.itemView?.item_friends_avatar)
        }
        holder.itemView?.item_friends_send_button?.setOnClickListener {
            mContext.startActivity(AddNotificationActivity.getStartingIntent(mContext)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun getItemCount(): Int = mFriends.size

    /**
     * Put data in the recycler view.
     */
    fun setData(friends: List<User>) {
        mFriends = friends
        notifyDataSetChanged()
    }

    /**
     * View holder of the friend item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}