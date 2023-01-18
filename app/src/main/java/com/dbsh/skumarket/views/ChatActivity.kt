package com.dbsh.skumarket.views

import android.view.MenuItem
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.base.LinearLayoutManagerWrapper
import com.dbsh.skumarket.databinding.ActivityChatBinding
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.viewmodels.ChatViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatActivity: BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    lateinit var chatList: ArrayList<Chat>
    private val auth = Firebase.auth
    private val uid = auth.currentUser?.uid

    override fun init() {
        viewModel = ChatViewModel()
        binding.apply {
            viewModel = viewModel
        }

        chatList = ArrayList()
        adapter = ChatAdapter(chatList, uid.toString())
        adapter.apply {

        }

        // 툴바
        val opponent = intent.getStringExtra("opponent").toString()
        binding.chatOpponentName.text = opponent
        setSupportActionBar(binding.chatToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.chatRecyclerview.adapter = adapter
        binding.chatRecyclerview.layoutManager = LinearLayoutManagerWrapper(this)
        binding.chatRecyclerview.scrollToPosition(adapter.itemCount-1)

        // 채팅방 로드
        val roomId = intent.getStringExtra("roomId").toString()
        viewModel.loadChat(roomId.toString())

        // 채팅 보내기
        binding.chatSend.setOnClickListener {
            if(binding.chatInput.text.toString().isNotBlank()) {
                viewModel.sendMessage(roomId, binding.chatInput.text.toString())
                binding.chatInput.text.clear()
            }
        }

        // 채팅 갱신
        viewModel.chatList.observe(this) {
            chatList.clear()
            if (it != null) {
                for(chat in it) {
                    chatList.add(chat)
                    adapter.notifyItemInserted(adapter.itemCount)
                    binding.chatRecyclerview.scrollToPosition(adapter.itemCount-1)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                finish()
        }
        return super.onOptionsItemSelected(item)
    }
}