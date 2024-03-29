package com.dbsh.skumarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.databinding.ItemChatBinding
import com.dbsh.skumarket.api.model.Chat

class ChatAdapter(data: ArrayList<Chat>, uid: String, profileImage: String) : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {
    private var _data: ArrayList<Chat> = data
    private var _uid = uid
    private var _profileImage = profileImage
    private var mClickable: Boolean? = null

    fun dataClear() {
        _data.clear()
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

    inner class ListViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Chat) {
            binding.chat = item
            var isImage = false

            if (item.imageUrl.toString() != "") {
                isImage = true
            }

            if (item.uid == _uid) {
                // 이미지 또는 텍스트 체크
                if (isImage) {
                    Glide.with(binding.root.context).load(item.imageUrl).into(binding.chatMyImage)
                    binding.chatMyImage.visibility = View.VISIBLE
                    binding.chatOtherImage.visibility = View.GONE
                    binding.chatMyText.visibility = View.GONE
                } else {
                    binding.chatMyImage.visibility = View.GONE
                    binding.chatOtherImage.visibility = View.GONE
                    binding.chatMyText.visibility = View.VISIBLE
                }
                binding.chatMyNickName.visibility = View.VISIBLE
                binding.chatMyTime.visibility = View.VISIBLE
                binding.chatMyBlock.visibility = View.VISIBLE

                binding.chatOtherProfileImage.visibility = View.GONE
                binding.chatOtherNickName.visibility = View.GONE
                binding.chatOtherTime.visibility = View.GONE
                binding.chatOtherBlock.visibility = View.GONE
            } else {
                // 이미지 또는 텍스트 체크
                if (isImage) {
                    Glide.with(binding.root.context).load(item.imageUrl).into(binding.chatOtherImage)
                    binding.chatOtherImage.visibility = View.VISIBLE
                    binding.chatMyImage.visibility = View.GONE
                    binding.chatOtherText.visibility = View.GONE
                } else {
                    binding.chatOtherImage.visibility = View.GONE
                    binding.chatMyImage.visibility = View.GONE
                    binding.chatOtherText.visibility = View.VISIBLE
                }

                if (_profileImage != "") {
                    Glide.with(binding.root).load(_profileImage).circleCrop().into(binding.chatOtherProfileImage)
                }
                binding.chatMyNickName.visibility = View.GONE
                binding.chatMyTime.visibility = View.GONE
                binding.chatMyBlock.visibility = View.GONE

                binding.chatOtherProfileImage.visibility = View.VISIBLE
                binding.chatOtherNickName.visibility = View.VISIBLE
                binding.chatOtherTime.visibility = View.VISIBLE
                binding.chatOtherBlock.visibility = View.VISIBLE
            }
        }
    }
}
