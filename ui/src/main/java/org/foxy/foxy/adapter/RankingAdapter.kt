package org.foxy.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_friends.view.*
import kotlinx.android.synthetic.main.item_ranking.view.*
import org.foxy.data.model.User
import org.foxy.foxy.R
import kotlin.collections.ArrayList

/**
 * Adapter used to display ranking.
 */
class RankingAdapter(val mContext: Context) : RecyclerView.Adapter<RankingAdapter.ItemViewHolder>() {

    private var mUsers: List<User> = ArrayList()
    private var mScores: List<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ranking, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_ranking_position?.text = (position+1).toString()
        holder.itemView?.item_ranking_user_name?.text = "Billy Bob, Le seul et l'unique"
        holder.itemView?.item_ranking_avatar?.setImageResource(R.drawable.ic_placeholder_male)
        /*
        if (mUsers[position].avatar.isNullOrEmpty()) {
            holder.itemView?.item_friends_avatar?.setImageResource(R.drawable.ic_placeholder_male)
        } else {
            Glide.with(mContext).load(mUsers[position].avatar).into(holder.itemView?.item_friends_avatar)
        }
        */
        holder.itemView?.item_ranking_score?.text = "36.541 pts"
    }

    override fun getItemCount(): Int = 50

    /**
     * Put data in the recycler view.
     */
    fun setData(users: List<User>, scores: List<Int>) {
        mUsers = users
        mScores = scores
        notifyDataSetChanged()
    }

    /**
     * View holder of the global rank item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}