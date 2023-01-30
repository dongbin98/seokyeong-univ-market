package com.dbsh.skumarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ItemChatListBinding
import com.dbsh.skumarket.model.ChatListDto

class ChatListAdapter(data: ArrayList<ChatListDto>) : RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {
    var mData: ArrayList<ChatListDto> = data
    private var mClickable: Boolean? = null
    private var itemClickListener: OnItemClickListener? = null
    private var itemLongClickListener: OnItemLongClickListener? = null

    fun dataClear() {
        mData.clear()
    }

    // 대화목록 클릭 처리부
    fun setAdapterClickable(clickable: Boolean) {
        mClickable = clickable
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ChatListDto, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(v: View, data: ChatListDto, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ListViewHolder(private val binding: ItemChatListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatListDto) {
            binding.lastChat = item

            if(item.opponentImage.isNullOrBlank())
                Glide.with(binding.root).load(R.drawable.default_profile_img).circleCrop().into(binding.chatListProfileImage)
            else
                Glide.with(binding.root).load(item.opponentImage).circleCrop().into(binding.chatListProfileImage)

            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    itemClickListener?.onItemClick(itemView, item, position)
                }
                itemView.setOnLongClickListener {
                    itemLongClickListener?.onItemLongClick(itemView, item, position)
                    true
                }
            }
        }
    }
}