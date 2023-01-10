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

class ChatListFragment : Fragment() {
    companion object{
        const val TAG = "ChatList Fragment"
    }

    private lateinit var binding: FragmentChatListBinding
    private lateinit var viewModel: ChatListViewModel
    lateinit var adapter: ChatListAdapter
    lateinit var chatList: ArrayList<LastChatData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)
        viewModel = ChatListViewModel()
        binding.apply {
            viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }

        // RecyclerView Setting
        chatList = ArrayList()
        adapter = ChatListAdapter(chatList)
        binding.chatRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerview.adapter = adapter

        // 임시 데이터 삽입
        chatList.add(LastChatData("염동빈", "22-10-28", "좋은 거래였습니다\n다시는 안합니다"))
        chatList.add(LastChatData("양승협", "22-10-27", "댁만 좋았나본데요"))
        chatList.add(LastChatData("박태룡", "22-10-26", "니들 다 F다"))
        adapter.notifyDataSetChanged()

        return binding.root
    }
}