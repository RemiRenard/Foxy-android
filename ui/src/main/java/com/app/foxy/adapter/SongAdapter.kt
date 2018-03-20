package com.app.foxy.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.data.model.Song
import com.app.foxy.R
import com.app.foxy.eventBus.SongSelectedNotifEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    private var mForceStopPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if(position == mForceStopPosition){
            holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play_white)
            mForceStopPosition = -1
        }
        Glide.with(mContext).asGif().load(mSongs[position].picture).apply(RequestOptions
                .placeholderOf(R.drawable.ic_placeholder_square_gray))
                .into(holder.itemView.item_song_image_view)
        holder.itemView.setOnLongClickListener {
            songSelected(holder, position)
        }
        holder.itemView.item_song_audio_button.setOnLongClickListener {
            songSelected(holder, position)
        }
        holder.itemView.item_song_audio_button.setOnClickListener({
            if (!mIsPlaying) {
                playSong(holder, position)
            } else {
                //If an other notification's audio is currently played
                if (mPositionPlaying == position) {
                    stopSong(holder)
                } else {
                    mForceStopPosition = mPositionPlaying
                    notifyItemChanged(mForceStopPosition)
                    stopSong(holder)
                    playSong(holder, position)
                }
            }
        })
    }

    private fun stopSong(holder: ItemViewHolder) {
        mPlayer?.release()
        mIsPlaying = false
        mPositionPlaying = -1
        holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play_white)
    }

    private fun playSong(holder: ItemViewHolder, position: Int) {
        mIsPlaying = true
        mPositionPlaying = position
        //If nothing is currently played
        holder.itemView.item_song_progress_bar.visibility = View.VISIBLE
        holder.itemView.item_song_audio_button.visibility = View.INVISIBLE
        mPlayer = MediaPlayer()
        try {
            mPlayer?.setDataSource(mContext, Uri.parse(mSongs[position].url))
            mPlayer?.prepareAsync()
            mPlayer?.setOnPreparedListener {
                holder.itemView.item_song_progress_bar.visibility = View.INVISIBLE
                holder.itemView.item_song_audio_button.visibility = View.VISIBLE
                holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_stop_white)
                mPlayer?.start()
            }
            mPlayer?.setOnCompletionListener {
                mPlayer = null
                holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play_white)
                mIsPlaying = false
                mPositionPlaying = -1
            }
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, e.message)
        } catch (e: IllegalStateException) {
            Log.e(javaClass.simpleName, e.message)
        }
    }

    private fun songSelected(holder: ItemViewHolder, position: Int): Boolean {
        holder.itemView.item_song_image_view.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGreenTransparent))
        EventBus.getDefault().post(SongSelectedNotifEvent(mSongs[position]))
        return true
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