package com.app.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.data.model.User
import com.app.foxy.R
import com.app.foxy.eventBus.AddFriendsIconClickedEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_add_friends.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Adapter used to display users.
 */
class AddFriendsAdapter : RecyclerView.Adapter<AddFriendsAdapter.ItemViewHolder>() {

    private var mContext: Context? = null
    private var mUsers: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        return ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_add_friends, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_add_friends_name?.text = mUsers[position].username
        holder.itemView?.item_add_friends_icon?.setOnClickListener {
            EventBus.getDefault().post(AddFriendsIconClickedEvent(mUsers[position]))
        }
        holder.itemView?.setOnClickListener {
            EventBus.getDefault().post(AddFriendsIconClickedEvent(mUsers[position]))
        }
        if (mUsers[position].avatar.isNullOrEmpty()) {
            holder.itemView?.item_add_friends_avatar?.setImageResource(R.drawable.ic_placeholder_male_white)
        } else {
            Glide.with(mContext).load(mUsers[position].avatar).apply(RequestOptions
                    .placeholderOf(R.drawable.ic_placeholder_circle_white))
                    .into(holder.itemView?.item_add_friends_avatar)
        }
    }

    override fun getItemCount(): Int = mUsers.size

    /**
     * Put data in the recycler view.
     */
    fun setData(users: List<User>) {
        mUsers.clear()
        mUsers.addAll(users)
        notifyDataSetChanged()
    }

    /**
     * Remove a user from the recycler view.
     */
    fun requestSent(user: User) {
        mUsers.remove(user)
        notifyDataSetChanged()
    }

    /**
     * View holder of the notification item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}