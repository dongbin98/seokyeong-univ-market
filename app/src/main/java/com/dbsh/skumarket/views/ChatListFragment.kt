package com.dbsh.skumarket.views

import android.annotation.SuppressLint
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
    lateinit var chatRoomList: ArrayList<ChatListDto>

    @SuppressLint("NotifyDataSetChanged")
    override fun init() {
        viewModel = ChatListViewModel()
        binding.apply {
            viewModel = viewModel
        }
        // RecyclerView Setting
        chatRoomList = ArrayList()
        adapter = ChatListAdapter(chatRoomList)
        adapter.apply {

        }

        binding.chatListRecyclerview.adapter = adapter
        binding.chatListRecyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.loadChatRoom()

        binding.chatTestButton.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }

        viewModel.chatList.observe(this) {
            if(it != null) {
                chatRoomList.clear()
                adapter.dataClear()
//                adapter.notifyDataSetChanged()
                for(chatRoom in it) {
                    chatRoomList.add(chatRoom)
                    adapter.notifyItemInserted(chatRoomList.size-1)
                }
            }
        }
    }
}