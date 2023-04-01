package com.example.application.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.data.network.ImageResponse
import com.example.application.databinding.ItemImageBinding

class ImageAdapter : ListAdapter<ImageResponse, ImageAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageResponse) {
            Glide.with(binding.root)
                .load(item.download_url)
                .into(binding.imvDestination)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageResponse>() {
        override fun areItemsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}