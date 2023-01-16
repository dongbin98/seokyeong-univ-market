package com.dbsh.skumarket.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.model.ChatRoom
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel: ViewModel() {
    var chatList: MutableLiveData<ArrayList<Chat>> = MutableLiveData()
    private val auth by lazy { Firebase.auth }
    private val userRef = Firebase.database.reference.child("User")
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private var uid: String = auth.uid.toString()
    private lateinit var _opponent: String
    private lateinit var _message: String

    // 메시지 보내기
    @SuppressLint("SimpleDateFormat")
    private fun sendMessage(chatRoomUid: String) {
        println("########### sendMessage() ###########")
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        userRef.child("users").child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val myName = snapshot.getValue<User>()?.name.toString()

                val chat = Chat(uid, _message, curTime, myName)
                println("chatRoomUid = $chatRoomUid")
                chatRef.child("chatRooms").child(chatRoomUid).child("messages").push().setValue(chat)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 채팅방 유무 체크
    fun checkChatRoom(opponent: String, message: String) {
        println("########### checkChatRoom() ###########")
        var chatRoomUid: String? = null
        _opponent = opponent
        _message = message
        chatRef.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children) {
                    if(item.getValue<ChatRoom>()?.users?.containsKey(opponent) == true) {
                        chatRoomUid = item.key
                    }
                }
                if(chatRoomUid != null) {
                    // 채팅방 존재 시 메시지 보냄
                    sendMessage(chatRoomUid.toString())
                } else {
                    // 채팅방 미존재 시 채팅방 생성
                    createChatRoom()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 채팅방 생성
    private fun createChatRoom() {
        println("########### createChatRoom() ###########")

        // 채팅 상대 이름 가져오기
        userRef.child("users").child(_opponent).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println("상대방 : ${snapshot.getValue<User>()?.name}")
                val otherOne = snapshot.getValue<User>()?.name.toString()

                val chatRoom = ChatRoom()
                chatRoom.users[uid] = true
                chatRoom.users[_opponent] = true
                chatRoom.otherOne = otherOne

                val chatRoomUid: String? = chatRef.child("chatRooms").push().key
                chatRef.child("chatRooms").child(chatRoomUid.toString()).setValue(chatRoom)

                // 채팅방 생성 후 메시지 보냄
                sendMessage(chatRoomUid.toString())
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 채팅 불러오기
    fun loadChat(opponent: String) {
        chatRef.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataList = ArrayList<Chat>()

                    for(chat in snapshot.child(opponent).child("messages").children) {
                        dataList.add(Chat(chat.child("uid").value.toString(), chat.child("message").value.toString(), chat.child("time").value.toString(), chat.child("name").value.toString()))
                    }
                    chatList.value = dataList
                }
            })
    }
}