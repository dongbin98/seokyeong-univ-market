package com.dbsh.skumarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbsh.skumarket.databinding.ItemChatBinding
import com.dbsh.skumarket.model.Chat

class ChatAdapter(data: ArrayList<Chat>, uid: String) : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {
    private var _data: ArrayList<Chat> = data
    private var _uid = uid
    private var mClickable: Boolean? = null

    fun dataClear() {
        _data.clear()
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
        holder.bind(_data[position])
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    inner class ListViewHolder(private val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Chat) {
            binding.chat = item
            if(item.uid.equals(_uid)) {
                binding.chatMyNickName.visibility = View.VISIBLE
                binding.chatMyTime.visibility = View.VISIBLE
                binding.chatMyBlock.visibility = View.VISIBLE

                binding.chatOtherProfileImage.visibility = View.GONE
                binding.chatOtherNickName.visibility = View.GONE
                binding.chatOtherTime.visibility = View.GONE
                binding.chatOtherBlock.visibility = View.GONE

                binding.chatMyImage.visibility = View.GONE
                binding.chatOtherImage.visibility = View.GONE
            } else {
                binding.chatMyNickName.visibility = View.GONE
                binding.chatMyTime.visibility = View.GONE
                binding.chatMyBlock.visibility = View.GONE

                binding.chatOtherProfileImage.visibility = View.VISIBLE
                binding.chatOtherNickName.visibility = View.VISIBLE
                binding.chatOtherTime.visibility = View.VISIBLE
                binding.chatOtherBlock.visibility = View.VISIBLE

                binding.chatMyImage.visibility = View.GONE
                binding.chatOtherImage.visibility = View.GONE
            }
        }
    }
}