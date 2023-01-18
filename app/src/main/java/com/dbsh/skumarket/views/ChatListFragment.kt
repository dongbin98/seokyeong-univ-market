package com.dbsh.skumarket.views

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatListAdapter
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.base.LinearLayoutManagerWrapper
import com.dbsh.skumarket.databinding.FragmentChatListBinding
import com.dbsh.skumarket.model.ChatListDto
import com.dbsh.skumarket.viewmodels.ChatListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {
    companion object{
        const val TAG = "ChatList Fragment"
    }

    private lateinit var viewModel: ChatListViewModel
    private lateinit var adapter: ChatListAdapter
    private lateinit var chatRoomList: ArrayList<ChatListDto>

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
            setOnItemClickListener(object: ChatListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, data: ChatListDto, position: Int) {
                    println("채팅방 : ${data.roomId} 입장")
                    Intent(context, ChatActivity::class.java).apply {
                        putExtra("roomId", data.roomId)
                        putExtra("opponent", data.otherOne)
                    }.run { startActivity(this) }
                }
            })
        }

        binding.chatListRecyclerview.adapter = adapter
        binding.chatListRecyclerview.layoutManager = LinearLayoutManagerWrapper(context)

        viewModel.loadChatRoom()

        viewModel.chatList.observe(this) {
            if(it != null) {
                chatRoomList.clear()
                adapter.dataClear()
//                adapter.notifyDataSetChanged()
                for(chatRoom in it) {
                    chatRoomList.add(chatRoom)
                    adapter.notifyItemInserted(adapter.itemCount)
                }
            }
        }
    }
}