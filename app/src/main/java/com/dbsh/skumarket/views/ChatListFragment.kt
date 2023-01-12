package com.dbsh.skumarket.views

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatListAdapter
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.databinding.FragmentChatListBinding
import com.dbsh.skumarket.model.ChatListDto
import com.dbsh.skumarket.model.ChatRoom
import com.dbsh.skumarket.viewmodels.ChatListViewModel

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {
    companion object{
        const val TAG = "ChatList Fragment"
    }

    private lateinit var viewModel: ChatListViewModel
    private lateinit var adapter: ChatListAdapter
    lateinit var chatList: ArrayList<ChatListDto>

    override fun init() {
        viewModel = ChatListViewModel()
        binding.apply {
            viewModel = viewModel
        }
        // RecyclerView Setting
        chatList = ArrayList()
        adapter = ChatListAdapter(chatList)
        adapter.apply {

        }

        binding.chatRecyclerview.adapter = adapter
        binding.chatRecyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.loadChatRoom()

        binding.chatTestButton.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }

        viewModel.chatList.observe(this) {
            if(it != null) {
                for(chat in it) {
                    chatList.add(chat)
                    adapter.notifyItemInserted(chatList.size-1)
                }
            }
        }
    }
}