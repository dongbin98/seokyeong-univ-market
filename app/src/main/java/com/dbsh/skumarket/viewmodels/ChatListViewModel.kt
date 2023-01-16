package com.dbsh.skumarket.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.model.ChatListDto
import com.dbsh.skumarket.model.ChatRoom
import com.dbsh.skumarket.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ChatListViewModel : ViewModel() {
    var chatList: MutableLiveData<ArrayList<ChatListDto>> = MutableLiveData()
    private val auth = Firebase.auth
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private val uid = auth.currentUser?.uid

    fun loadChatRoom() {
        chatRef.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = ArrayList<ChatListDto>()

                for(chatRoom in snapshot.children) {
                    println("chatRoom = $chatRoom")

                    var lastChat: ChatListDto? = null
                    var otherOne = chatRoom.child("otherOne").value.toString()

                    for(chat in chatRoom.child("messages").children) {
                        println("messasge in data = $chat")
                        lastChat = ChatListDto(chat.child("uid").value.toString(), chat.child("message").value.toString(), chat.child("time").value.toString(), otherOne)
                    }

                    if (lastChat != null) {
                        dataList.add(lastChat)
                    }
                }
                chatList.value = dataList
            }
        })
    }
}