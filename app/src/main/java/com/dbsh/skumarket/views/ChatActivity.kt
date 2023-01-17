package com.dbsh.skumarket.views

import androidx.recyclerview.widget.LinearLayoutManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatAdapter
import com.dbsh.skumarket.adapters.ChatListAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.base.LinearLayoutManagerWrapper
import com.dbsh.skumarket.databinding.ActivityChatBinding
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.model.ChatListDto
import com.dbsh.skumarket.viewmodels.ChatViewModel

class ChatActivity: BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    lateinit var chatList: ArrayList<Chat>

    override fun init() {
        viewModel = ChatViewModel()
        binding.apply {
            viewModel = viewModel
        }

        chatList = ArrayList()
        adapter = ChatAdapter(chatList)
        adapter.apply {

        }

        binding.chatRecyclerview.adapter = adapter
        binding.chatRecyclerview.layoutManager = LinearLayoutManagerWrapper(this)

        // 채팅방 로드
        val roomId = intent.getStringExtra("roomId").toString()
        viewModel.loadChat(roomId.toString())

        // 채팅 보내기
        binding.chatSend.setOnClickListener {
            viewModel.sendMessage(roomId, binding.chatInput.text.toString())
            binding.chatInput.text.clear()
        }

        viewModel.chatList.observe(this) {
            chatList.clear()
            adapter.dataClear()
            if (it != null) {
                for(chat in it) {
                    chatList.add(chat)
                    adapter.notifyItemInserted(chatList.size - 1)
                }
            }
        }
    }
}