package com.dbsh.skumarket.ui.chat

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ChatAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.util.LinearLayoutManagerWrapper
import com.dbsh.skumarket.databinding.ActivityChatBinding
import com.dbsh.skumarket.api.model.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    private var chatList = mutableListOf<Chat>()
    private val auth = Firebase.auth
    private val uid = auth.currentUser?.uid

    private var selectedImage: Uri? = null

    override fun init() {
        viewModel = ChatViewModel()

        // 방 정보 전달받음
        var roomId = intent.getStringExtra("roomId")
        val opponentImage = intent.getStringExtra("opponentImage").toString()
        val opponent = intent.getStringExtra("opponent").toString()
        val opponentId = intent.getStringExtra("opponentId").toString()

        chatList = ArrayList()
        adapter = ChatAdapter(chatList as ArrayList<Chat>, uid.toString(), opponentImage)

        // 툴바
        binding.chatOpponentName.text = opponent
        setSupportActionBar(binding.chatToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.chatRecyclerview.apply {
            itemAnimator = null
            adapter = this@ChatActivity.adapter
            layoutManager =
                LinearLayoutManagerWrapper(this@ChatActivity)
            scrollToPosition(this@ChatActivity.adapter.itemCount - 1)
        }

        // 채팅방 로드
        if(roomId != null)
            viewModel.loadChat(roomId)
        else
            viewModel.checkChatRoom(opponentId)

        // Image Add Callback 등록
        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data?.data != null) {
                    selectedImage = it.data?.data
                    if (roomId != null) {
                        viewModel.sendImage(roomId!!, selectedImage!!)
                    }
                }
            }
        }

        // 채팅 보내기
        binding.chatSend.setOnClickListener {
            if (binding.chatInput.text.toString().isNotBlank()) {
                if (roomId != null) {
                    viewModel.sendMessage(roomId!!, binding.chatInput.text.toString())
                }
                binding.chatInput.text.clear()
            }
        }

        // 이미지 보내기
        binding.chatAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.run {
                getResult.launch(this)
            }
        }

        // 채팅방 ID 갱신
        viewModel.roomId.observe(this) {
            roomId = it
            viewModel.loadChat(roomId.toString())
        }

        // 채팅 갱신
        viewModel.chatList.observe(this) {
            chatList.clear()
            if (it != null) {
                for (chat in it) {
                    chatList.add(chat)
                    adapter.notifyItemInserted(adapter.itemCount)
                    binding.chatRecyclerview.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
