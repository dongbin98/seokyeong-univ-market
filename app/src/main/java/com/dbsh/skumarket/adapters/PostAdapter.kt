package com.dbsh.skumarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.api.model.PostList
import com.dbsh.skumarket.databinding.ItemPostListBinding


class PostAdapter(data: ArrayList<PostList>) : RecyclerView.Adapter<PostAdapter.ListViewHolder>() {

    var mData = data
    private var itemClickListener: OnItemClickListener? = null

    fun dataClear() {
        mData.clear()
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: PostList, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ListViewHolder(private val binding: ItemPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostList) {
            binding.posts = item

            if (item.image.isNullOrBlank()) {
                Glide.with(binding.root).load(R.drawable.default_profile_img).into(binding.postListImage)
            } else {
                Glide.with(binding.root).load(item.image).into(binding.postListImage)
            }

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    itemClickListener?.onItemClick(itemView, item, position)
                }
            }
        }
    }
}
