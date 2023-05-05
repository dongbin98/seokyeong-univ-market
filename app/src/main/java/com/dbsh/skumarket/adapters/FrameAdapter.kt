package com.dbsh.skumarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.databinding.ItemPostFrameBinding

class FrameAdapter(private val list: MutableMap<String, String>): RecyclerView.Adapter<FrameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemPostFrameBinding.inflate(inflater, parent, false)
        return FrameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(list[position.toString()]!!)
    }

    override fun getItemCount() = list.size
}

class FrameViewHolder(private val binding: ItemPostFrameBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(image: String) {
        Glide.with(binding.root).load(image).into(binding.postFrameImage)
    }
}