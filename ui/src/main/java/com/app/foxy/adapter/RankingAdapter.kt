package com.app.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.data.model.UserRank
import com.app.foxy.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_ranking.view.*

/**
 * Adapter used to display ranking.
 */
class RankingAdapter : RecyclerView.Adapter<RankingAdapter.ItemViewHolder>() {

    private var mContext: Context? = null
    private var mUserRanks: List<UserRank> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        return ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ranking, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_ranking_position?.text = (position + 1).toString()
        holder.itemView?.item_ranking_user_name?.text = mUserRanks[position].username
        if (mUserRanks[position].avatar.isNullOrEmpty()) {
            holder.itemView?.item_ranking_avatar?.setImageResource(R.drawable.ic_placeholder_male_white)
        } else {
            Glide.with(mContext)
                    .load(mUserRanks[position].avatar)
                    .apply(RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.ic_placeholder_circle_white))
                    .into(holder.itemView?.item_ranking_avatar)
        }
        holder.itemView?.item_ranking_score?.text = mUserRanks[position].score.toString()
    }

    override fun getItemCount(): Int = mUserRanks.size

    /**
     * Put data in the recycler view.
     */
    fun setData(userRanks: List<UserRank>) {
        mUserRanks = userRanks
        notifyDataSetChanged()
    }

    /**
     * View holder of the global rank item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}