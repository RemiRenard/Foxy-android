package org.foxy.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_friends.view.*
import org.foxy.data.model.User
import org.foxy.foxy.R
import java.util.*

/**
 * Adapter used to display global ranking.
 */
class RankingGlobalAdapter(val mContext: Context) : RecyclerView.Adapter<RankingGlobalAdapter.ItemViewHolder>() {

    private var mUsers: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ranking, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_friends_name?.text = mUsers[position].username
        if (mUsers[position].avatar.isNullOrEmpty()) {
            holder.itemView?.item_friends_avatar?.setImageResource(R.drawable.ic_placeholder_male)
        } else {
            Glide.with(mContext).load(mUsers[position].avatar).into(holder.itemView?.item_friends_avatar)
        }
    }

    override fun getItemCount(): Int = mUsers.size

    /**
     * Put data in the recycler view.
     */
    fun setData(users: List<User>) {
        mUsers = users
        notifyDataSetChanged()
    }

    /**
     * View holder of the global rank item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}