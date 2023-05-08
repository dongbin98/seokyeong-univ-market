package com.dbsh.skumarket.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.databinding.ItemPostImageBinding
import kotlin.coroutines.coroutineContext

class ImageAdapter(
    private val list: MutableList<Uri>, private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener {
        fun deleteImage(position: Int)
    }

    inner class ImageViewHolder(private val binding: ItemPostImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri) {
            Glide.with(binding.root).load(item).into(binding.itemImage)
            binding.itemImageDeleteButton.setOnClickListener {
                itemClickListener.deleteImage(adapterPosition)
            }
        }
    }
}