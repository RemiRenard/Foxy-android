package com.app.foxy.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.app.data.model.Song
import com.app.foxy.R
import com.app.foxy.eventBus.SongSelectedNotifEvent
import kotlinx.android.synthetic.main.item_song.view.*
import org.greenrobot.eventbus.EventBus
import java.io.IOException

/**
 * Adapter used to display songs.
 */
class SongAdapter(private val mContext: Context) : RecyclerView.Adapter<SongAdapter.ItemViewHolder>() {

    private var mSongs: List<Song> = ArrayList()
    private var mPlayer: MediaPlayer? = null
    private var mIsPlaying: Boolean = false
    private var mPositionPlaying: Int = -1
    private var mSongSelected: Song? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (mSongSelected == mSongs[position]) {
            holder.itemView.item_song_text_view.setTextColor(Color.BLACK)
            holder.itemView.item_song_text_view.setTypeface(null, Typeface.BOLD)
        } else {
            holder.itemView.item_song_text_view.setTextColor(Color.GRAY)
            holder.itemView.item_song_text_view.setTypeface(null, Typeface.NORMAL)
        }
        Glide.with(mContext).asGif().load(mSongs[position].picture).apply(RequestOptions
                .placeholderOf(R.drawable.ic_placeholder_square_blue))
                .into(holder.itemView.item_song_image_view)
        holder.itemView.item_song_text_view.text = mSongs[position].name
        holder.itemView.setOnClickListener {
            mSongSelected = if (mSongSelected == mSongs[position]) {
                Song()
            } else {
                mSongs[position]
            }
            EventBus.getDefault().post(SongSelectedNotifEvent(mSongSelected!!))
            notifyDataSetChanged()
        }
        holder.itemView.item_song_audio_button.setOnClickListener({
            if (!mIsPlaying) {
                mIsPlaying = true
                mPositionPlaying = position
                //If nothing is currently played
                holder.itemView.item_song_progress_bar.visibility = View.VISIBLE
                holder.itemView.item_song_audio_button.visibility = View.GONE
                mPlayer = MediaPlayer()
                try {
                    mPlayer?.setDataSource(mContext, Uri.parse(mSongs[position].url))
                    mPlayer?.prepareAsync()
                    mPlayer?.setOnPreparedListener {
                        holder.itemView.item_song_progress_bar.visibility = View.GONE
                        holder.itemView.item_song_audio_button.visibility = View.VISIBLE
                        holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_stop)
                        mPlayer?.start()
                    }
                    mPlayer?.setOnCompletionListener {
                        mPlayer = null
                        holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play)
                        mIsPlaying = false
                        mPositionPlaying = -1
                    }
                } catch (e: IOException) {
                    Log.e(javaClass.simpleName, e.message)
                } catch (e: IllegalStateException) {
                    Log.e(javaClass.simpleName, e.message)
                }
            } else {
                //If an other notification's audio is currently played
                if (mPositionPlaying == position) {
                    mPlayer?.release()
                    mIsPlaying = false
                    mPositionPlaying = -1
                    holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play)
                } else {
                    Toast.makeText(mContext, R.string.stop_audio_first, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun getItemCount(): Int = mSongs.size

    fun setData(songs: List<Song>) {
        mSongs = songs
        notifyDataSetChanged()
    }

    /**
     * View holder of the song item.
     */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}