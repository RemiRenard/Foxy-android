package com.app.foxy.notification.selectSong.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.data.model.Song
import com.app.foxy.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_song.view.*

/**
 * Adapter used to display songs.
 */
class SongAdapter(val mPresenter: ISongAdapterPresenter) : RecyclerView.Adapter<SongAdapter.ItemViewHolder>(), ISongAdapterView {

    private var mContext: Context? = null
    private var mSongs: List<Song> = ArrayList()
    private var mPositionPlaying: Int = -1
    private var mPositionLoading: Int = -1
    private var mPreviousPositionLoading: Int = -1
    private var mPreviousPositionPlaying: Int = -1
    private var mIsPlaying: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        return ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false))
    }

    fun onResume() {
        mPresenter.attachView(this)
    }

    fun onPause() {
        mPresenter.detachView()
    }

    override fun updateItems() {
        //notifyDataSetChanged()
    }

    override fun itemPlayingCompleted() {
        mPositionPlaying = -1
        mIsPlaying = false
        notifyDataSetChanged()
    }

    override fun itemLoadingCompleted() {
        mPositionPlaying = mPositionLoading
        mPositionLoading = -1
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (mPositionLoading == position) {
            holder.itemView.item_song_audio_button.visibility = View.GONE
            holder.itemView.item_song_progress_bar.visibility = View.VISIBLE
        } else if (mPreviousPositionLoading == position || mPreviousPositionPlaying == position) {
            mPreviousPositionPlaying = -1
            mPreviousPositionLoading = -1
            holder.itemView.item_song_audio_button.visibility = View.VISIBLE
            holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play_white)
            holder.itemView.item_song_progress_bar.visibility = View.GONE
        } else if (mPositionPlaying == position) {
            holder.itemView.item_song_audio_button.visibility = View.VISIBLE
            holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_stop_white)
            holder.itemView.item_song_progress_bar.visibility = View.GONE
        } else {
            holder.itemView.item_song_audio_button.visibility = View.VISIBLE
            holder.itemView.item_song_audio_button.setImageResource(R.drawable.ic_play_white)
            holder.itemView.item_song_progress_bar.visibility = View.GONE
        }
        Glide.with(mContext).asGif().load(mSongs[position].picture).apply(RequestOptions
                .placeholderOf(R.drawable.ic_placeholder_square_gray))
                .into(holder.itemView.item_song_image_view)
        holder.itemView.setOnLongClickListener {
            songLongClicked(holder)
        }
        holder.itemView.item_song_audio_button.setOnLongClickListener {
            songLongClicked(holder)
        }
        holder.itemView.item_song_audio_button.setOnClickListener({
            if (!mIsPlaying && position != mPositionLoading && position != mPositionPlaying) {
                mPositionLoading = holder.adapterPosition
                mIsPlaying = true
                mPresenter.playSong(mSongs[position])
            } else if (mIsPlaying && position != mPositionPlaying && position != mPositionPlaying) {
                mPresenter.stopSong()
                mPreviousPositionLoading = mPositionLoading
                mPreviousPositionPlaying = mPositionPlaying
                mPositionLoading = holder.adapterPosition
                mIsPlaying = true
                mPresenter.playSong(mSongs[position])
            } else if (mIsPlaying) {
                mPositionPlaying = -1
                mIsPlaying = false
                mPresenter.stopSong()
            }
            notifyDataSetChanged()
        })
    }

    private fun songLongClicked(holder: ItemViewHolder): Boolean {
        holder.itemView.item_song_image_view.setColorFilter(ContextCompat.getColor(mContext!!, R.color.colorGreenTransparent))
        mPresenter.songSelected(mSongs[holder.adapterPosition])
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