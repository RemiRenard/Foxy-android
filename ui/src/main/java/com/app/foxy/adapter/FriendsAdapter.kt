package com.app.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.data.model.User
import com.app.foxy.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_friends.view.*
import java.util.*

/**
 * Adapter used to display friends.
 */
class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.ItemViewHolder>() {

    private var mContext: Context? = null
    private var mFriends: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        return ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friends, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_friends_name?.text = mFriends[position].username
        if (mFriends[position].avatar.isNullOrEmpty()) {
            holder.itemView?.item_friends_avatar?.setImageResource(R.drawable.ic_placeholder_male)
        } else {
            Glide.with(mContext).load(mFriends[position].avatar).into(holder.itemView?.item_friends_avatar)
        }
        holder.itemView.setOnClickListener {
            Toast.makeText(mContext, "Not implemented yet :/", Toast.LENGTH_SHORT).show()
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