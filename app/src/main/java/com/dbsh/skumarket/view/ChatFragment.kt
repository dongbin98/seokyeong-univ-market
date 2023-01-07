package com.dbsh.skumarket.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.FragmentChatBinding
import com.dbsh.skumarket.viewmodels.ChatViewModel

class ChatFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        viewModel = ChatViewModel()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel

        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}