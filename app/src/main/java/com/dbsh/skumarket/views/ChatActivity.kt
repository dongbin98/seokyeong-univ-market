package com.dbsh.skumarket.views

import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityChatBinding
import com.dbsh.skumarket.viewmodels.ChatViewModel

class ChatActivity: BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private lateinit var viewModel: ChatViewModel
    override fun init() {
        viewModel = ChatViewModel()
        binding.apply {
            viewModel = viewModel
        }

        viewModel.checkChatRoom("gWybQ8cb0zgx07LfAiCiJiOUn723", "안녕하세요")
    }
}