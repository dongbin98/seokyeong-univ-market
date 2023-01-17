package com.dbsh.skumarket.views

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatListAdapter
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.base.LinearLayoutManagerWrapper
import com.dbsh.skumarket.databinding.FragmentChatListBinding
import com.dbsh.skumarket.model.ChatListDto
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
            setOnItemClickListener(object: ChatListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, data: ChatListDto, position: Int) {
                    println("채팅방 : ${data.roomId} 입장")
                    Intent(context, ChatActivity::class.java).apply {
                        putExtra("roomId", data.roomId)
                    }.run { startActivity(this) }
                }
            })
        }

        binding.chatListRecyclerview.adapter = adapter
        binding.chatListRecyclerview.layoutManager = LinearLayoutManagerWrapper(context)

        viewModel.loadChatRoom()

        binding.chatTestButton.setOnClickListener {
            Intent(context, ChatActivity::class.java).run { startActivity(this) }
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