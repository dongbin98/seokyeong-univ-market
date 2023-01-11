package com.dbsh.skumarket.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapter.ChatListAdapter
import com.dbsh.skumarket.databinding.FragmentChatListBinding
import com.dbsh.skumarket.model.LastChatData
import com.dbsh.skumarket.viewmodels.ChatListViewModel

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {
    companion object{
        const val TAG = "ChatList Fragment"
    }

    private lateinit var viewModel: ChatListViewModel
    lateinit var adapter: ChatListAdapter
    lateinit var chatList: ArrayList<LastChatData>

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
        chatList.add(LastChatData("염동빈", "22-10-28", "좋은 거래였습니다\n다시는 안합니다"))
        adapter.notifyItemInserted(chatList.size)
        chatList.add(LastChatData("양승협", "22-10-27", "댁만 좋았나본데요"))
        adapter.notifyItemInserted(chatList.size)
        chatList.add(LastChatData("박태룡", "22-10-26", "니들 다 F다"))
        adapter.notifyItemInserted(chatList.size)
    }

}