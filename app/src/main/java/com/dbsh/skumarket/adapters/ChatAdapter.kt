package com.dbsh.skumarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbsh.skumarket.databinding.ItemChatBinding
import com.dbsh.skumarket.databinding.ItemChatListBinding
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.model.ChatListDto

class ChatAdapter(data: ArrayList<Chat>) : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {
    var mData: ArrayList<Chat> = data
    private var mClickable: Boolean? = null

    fun dataClear() {
        mData.clear()
    }

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
        fun bind(item: Chat) {
            binding.chat = item
        }
    }
}