package org.foxy.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_friends.view.*
import org.foxy.data.model.User
import org.foxy.foxy.R
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
            Glide.with(mContext).load(mFriends[position].avatar).apply(RequestOptions
                    .bitmapTransform(RoundedCornersTransformation(46, 0, RoundedCornersTransformation.CornerType.LEFT))
                    .placeholder(R.drawable.ic_placeholder_circle_gray))
                    .into(holder.itemView?.item_friends_avatar)
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
     * View holder of the notification item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}