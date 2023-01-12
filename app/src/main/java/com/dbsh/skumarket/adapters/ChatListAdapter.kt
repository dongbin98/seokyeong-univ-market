package com.dbsh.skumarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbsh.skumarket.databinding.ItemChatBinding
import com.dbsh.skumarket.model.ChatListDto
import com.dbsh.skumarket.model.ChatRoom

class ChatListAdapter(data: ArrayList<ChatListDto>) : RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {
    var mData: ArrayList<ChatListDto> = data
    private var mClickable: Boolean? = null

    // 대화목록 클릭 처리부
    fun setAdapterClickable(clickable: Boolean) {
        mClickable = clickable
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ListViewHolder(private val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatListDto) {
            binding.lastChat = item
        }
    }
}