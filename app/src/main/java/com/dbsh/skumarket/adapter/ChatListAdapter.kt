package com.dbsh.skumarket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ItemChatBinding
import com.dbsh.skumarket.model.LastChatData

class ChatListAdapter(data: ArrayList<LastChatData>) : RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {
    var data: ArrayList<LastChatData> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ListViewHolder(private val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LastChatData) {
            binding.lastChat = item
        }
    }

}