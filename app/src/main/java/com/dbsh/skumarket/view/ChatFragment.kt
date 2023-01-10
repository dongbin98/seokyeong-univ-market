package com.dbsh.skumarket.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapter.ChatListAdapter
import com.dbsh.skumarket.databinding.FragmentChatBinding
import com.dbsh.skumarket.model.LastChatData
import com.dbsh.skumarket.viewmodels.ChatViewModel

class ChatFragment : Fragment() {
    companion object{
        const val TAG = "ChatFragment"
    }

    lateinit var binding: FragmentChatBinding
    private var viewModel: ChatViewModel = ChatViewModel()
    lateinit var ChatListAdapter: ChatListAdapter
    lateinit var item: ArrayList<LastChatData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.apply {
            viewModel = ChatViewModel()
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
        item = ArrayList()
        ChatListAdapter = ChatListAdapter(item)
        binding.chatRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerview.adapter = ChatListAdapter

        item.add(LastChatData("염동빈", "22-10-28", "좋은 거래였습니다\n다시는 안합니다"))
        item.add(LastChatData("양승협", "22-10-27", "댁만 좋았나본데요"))
        item.add(LastChatData("박태룡", "22-10-26", "니들 다 F다"))

        ChatListAdapter.notifyDataSetChanged()

        return binding.root
    }
}