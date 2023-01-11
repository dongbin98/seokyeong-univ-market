package com.dbsh.skumarket.views

import androidx.recyclerview.widget.LinearLayoutManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatListAdapter
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.databinding.FragmentChatListBinding
import com.dbsh.skumarket.model.LastChat
import com.dbsh.skumarket.viewmodels.ChatListViewModel

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {
    companion object{
        const val TAG = "ChatList Fragment"
    }

    private lateinit var viewModel: ChatListViewModel
    lateinit var adapter: ChatListAdapter
    lateinit var chatList: ArrayList<LastChat>

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

        // 임시 데이터 삽입
        chatList.add(LastChat("염동빈", "22-10-28", "좋은 거래였습니다\n다시는 안합니다"))
        adapter.notifyItemInserted(chatList.size)
        chatList.add(LastChat("양승협", "22-10-27", "댁만 좋았나본데요"))
        adapter.notifyItemInserted(chatList.size)
        chatList.add(LastChat("박태룡", "22-10-26", "니들 다 F다"))
        adapter.notifyItemInserted(chatList.size)
    }

}