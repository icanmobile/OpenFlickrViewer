package com.icanmobile.openflickrviewer.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.model.Photo
import kotlinx.android.synthetic.main.photo_list_item.view.*

class PhotoListAdapter(
    private val requestManager: GlideManager,
    private val interaction: Interaction? = null,
    private var photos: List<Photo> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photo_list_item,
                parent,
                false
            ),
            interaction,
            requestManager
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(photos[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun submitList(photos: List<Photo>) {
        DiffUtil.calculateDiff(PhotoDiffCallback(this.photos, photos))
            .dispatchUpdatesTo(this)
        this.photos = photos
    }

    class PhotoViewHolder(
        itemView: View,
        private val interaction: Interaction?,
        private val requestManager: GlideManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Photo) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            requestManager.setImage(item.url, itemView.photo_image)
            itemView.photo_title.text = item.title
        }
    }

    class PhotoDiffCallback(
        private val oldPhotos: List<Photo>,
        private val newPhotos: List<Photo>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldPhotos.size
        }

        override fun getNewListSize(): Int {
            return newPhotos.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPhotos[oldItemPosition] == newPhotos[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPhotos[oldItemPosition] == newPhotos[newItemPosition]
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Photo)
    }
}