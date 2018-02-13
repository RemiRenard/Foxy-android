package com.foxyApp.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_select_friends.view.*
import com.foxyApp.data.model.User
import com.foxyApp.foxy.R
import com.foxyApp.foxy.event_bus.FriendsSelectedNotifEvent
import org.greenrobot.eventbus.EventBus

/**
 * Adapter used to select which friend will receive a notification.
 */
class SelectFriendsAdapter(private val mContext: Context) : RecyclerView.Adapter<SelectFriendsAdapter.ItemViewHolder>() {

    private var mFriends = ArrayList<User>()
    private val mFriendsSelected = ArrayList<User>()

    val friendsSelected: List<User>
        get() = mFriendsSelected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_select_friends, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_select_friends_name?.text = mFriends[position].username
        holder.itemView?.item_select_friends_check?.setImageResource(if (mFriendsSelected.contains(mFriends[position]))
            R.drawable.ic_checked else R.drawable.ic_unchecked)
        holder.itemView.setOnClickListener {
            if (mFriendsSelected.contains(mFriends[position])) {
                mFriendsSelected.remove(mFriends[position])
            } else {
                mFriendsSelected.add(mFriends[position])
            }
            EventBus.getDefault().post(FriendsSelectedNotifEvent(mFriendsSelected))
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = mFriends.size

    /**
     * Put data in the recycler view.
     */
    fun setData(users: List<User>) {
        mFriends = users as ArrayList<User>
        notifyDataSetChanged()
    }

    /**
     * View holder of the friend item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
