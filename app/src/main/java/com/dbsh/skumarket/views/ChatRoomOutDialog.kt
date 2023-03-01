package com.dbsh.skumarket.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.dbsh.skumarket.databinding.DialogChatRoomOutBinding

class ChatRoomOutDialog(context: Context, var opponent: String, private val okCallBack: (String) -> Unit) : Dialog(context) {
    private lateinit var binding: DialogChatRoomOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogChatRoomOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(opponent)
    }

    private fun init(opponent: String) = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        chatRoomOutOpponent.text = opponent
        chatRoomOutBtn.setOnClickListener {
            okCallBack("yes")
            dismiss()
        }
    }
}
