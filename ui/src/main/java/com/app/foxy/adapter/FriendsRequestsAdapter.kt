package com.app.foxy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.foxy.R
import com.app.foxy.eventBus.FriendRequestClickedEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Adapter used to display friends requests.
 */
class FriendsRequestsAdapter(val mContext: Context) : RecyclerView.Adapter<FriendsRequestsAdapter.ItemViewHolder>() {

    private var mRequests: MutableList<FriendsRequestsResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friends_requests, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.username.text = mRequests[position].requestedBy?.username
        holder.accept.setOnClickListener {
            disableButtons(holder)
            EventBus.getDefault().post(FriendRequestClickedEvent(
                    true, mRequests[position].requestId!!, mRequests[position].notificationId!!, mRequests[position]))
        }
        holder.decline.setOnClickListener {
            disableButtons(holder)
            EventBus.getDefault().post(FriendRequestClickedEvent(
                    false, mRequests[position].requestId!!, mRequests[position].notificationId!!, mRequests[position]))
        }
    }

    /**
     * Disable buttons (accept & decline)
     */
    private fun disableButtons(holder: ItemViewHolder) {
        holder.decline.isEnabled = false
        holder.accept.isEnabled = false
    }

    override fun getItemCount(): Int = mRequests.size

    /**
     * Put data in the recycler view.
     */
    fun setData(requests: List<FriendsRequestsResponse>) {
        mRequests.addAll(requests)
        notifyDataSetChanged()
    }

    /**
     * Remove an item from the recycler view.
     */
    fun removeItem(request: FriendsRequestsResponse) {
        mRequests.remove(request)
        notifyDataSetChanged()
    }

    /**
     * View holder of the notification item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.friends_requests_accept)
        lateinit var accept: ImageView

        @BindView(R.id.friends_requests_decline)
        lateinit var decline: ImageView

        @BindView(R.id.friends_requests_username)
        lateinit var username: TextView

        init {
            // Bind views.
            ButterKnife.bind(this, itemView)
        }
    }
}