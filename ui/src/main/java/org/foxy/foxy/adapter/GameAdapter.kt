package org.foxy.foxy.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_game_left.view.*
import org.foxy.data.Constants.TYPE_LEFT
import org.foxy.data.Constants.TYPE_RIGHT
import org.foxy.data.model.Game
import org.foxy.foxy.R
import org.foxy.foxy.search.game.GameActivity
import java.util.*

/**
 * Adapter used to display notifications.
 */
class GameAdapter(val mContext: Context) : RecyclerView.Adapter<GameAdapter.ItemViewHolder>() {

    private var mGames: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(mContext).inflate(if (viewType == TYPE_LEFT) {
            R.layout.item_game_left
        } else {
            R.layout.item_game_right
        }, parent, false))
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView?.item_game_title?.text = mGames[position].title
        holder.itemView?.item_game_description?.text = mGames[position].description
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView?.item_game_description?.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        }
        Glide.with(mContext).load(mGames[position].picture).apply(RequestOptions
                .bitmapTransform(RoundedCornersTransformation(46, 0,
                        if (holder.itemViewType == TYPE_LEFT) {
                            RoundedCornersTransformation.CornerType.LEFT
                        } else {
                            RoundedCornersTransformation.CornerType.RIGHT
                        })))
                .into(holder.itemView?.item_game_picture)
        holder.itemView.setOnClickListener {
            mContext.startActivity(GameActivity.getStartingIntent(mContext, mGames[position])
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

    }

    override fun getItemCount(): Int = mGames.size

    /**
     * Return the type of the item.
     */
    override fun getItemViewType(position: Int): Int {
        return if (position % 2 != 0) {
            TYPE_LEFT
        } else {
            TYPE_RIGHT
        }
    }

    /**
     * Put data in the recycler view.
     */
    fun setData(games: List<Game>) {
        mGames = games
        notifyDataSetChanged()
    }

    /**
     * View holder of the notification item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.item_game_picture)
        lateinit var picture: ImageView

        @BindView(R.id.item_game_title)
        lateinit var title: TextView

        @BindView(R.id.item_game_description)
        lateinit var description: TextView

        init {
            // Bind views.
            ButterKnife.bind(this, itemView)
        }
    }
}