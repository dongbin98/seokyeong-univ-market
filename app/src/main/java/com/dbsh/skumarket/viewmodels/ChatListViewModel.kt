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
    private val db = Firebase.database.reference
    private val uid = auth.currentUser?.uid

    fun loadChatRoom() {
        db.child("ChatRoom").child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = ArrayList<ChatListDto>()

                for(chatRoom in snapshot.children) {
                    println("chatRoom = $chatRoom")

                    var tmpChat: ChatListDto? = null

                    for(chat in chatRoom.child("messages").children) {
                        println("messasge in data = $chat")
                        tmpChat = ChatListDto(chat.child("uid").value.toString(), chat.child("message").value.toString(), chat.child("time").value.toString()   )
                    }
                    if (tmpChat != null) {
                        dataList.add(tmpChat)
                    }
                }
                chatList.value = dataList
            }
        })
    }
}