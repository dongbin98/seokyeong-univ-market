package com.dbsh.skumarket.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.model.ChatRoom
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.model.ChatUser
import com.dbsh.skumarket.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel: ViewModel() {
    var chatList: MutableLiveData<ArrayList<Chat>> = MutableLiveData()
    private val auth by lazy { Firebase.auth }
    private val userRef = Firebase.database.reference.child("User")
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private val storageRef = Firebase.storage.reference.child("ChatRoom")
    private val uid = auth.currentUser?.uid
    private lateinit var _opponent: String
    private lateinit var _message: String

    // 메시지 보내기
    @SuppressLint("SimpleDateFormat")
    fun sendMessage(chatRoomUid: String, message: String) {
        Log.d(TAG, "########### sendMessage(${chatRoomUid}, ${message}) ###########")
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        userRef.child("users").child(uid.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myName = snapshot.getValue<User>()?.name.toString()

                val chat = Chat(uid.toString(), message, "", curTime, myName, false)
                println("chatRoomUid = $chatRoomUid")
                chatRef.child("chatRooms").child(chatRoomUid).child("messages").push().setValue(chat)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 이미지 보내기
    @SuppressLint("SimpleDateFormat")
    fun sendImage(chatRoomUid: String, image: Uri) {
        Log.d(TAG, "########### sendImage(${chatRoomUid}) ###########")
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        storageRef.child(chatRoomUid).child(curTime).putFile(image).addOnSuccessListener {
            storageRef.child(chatRoomUid).child(curTime).downloadUrl.addOnSuccessListener {
                userRef.child("users").child(uid.toString()).addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val myName = snapshot.getValue<User>()?.name.toString()
                        val myImage = it.toString()
                        val chat = Chat(uid.toString(), "", myImage, curTime, myName, false)
                        chatRef.child("chatRooms").child(chatRoomUid).child("messages").push().setValue(chat)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    // 채팅방 유무 체크
    fun checkChatRoom(opponent: String, message: String) {
        Log.d(TAG, "########### checkChatRoom(${opponent}, ${message}) ###########")

        var chatRoomUid: String? = null
        _opponent = opponent
        _message = message
        chatRef.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children) {
                    if(item.getValue<ChatRoom>()?.users?.containsKey(opponent) == true) {
                        chatRoomUid = item.key
                    }
                }
                if(chatRoomUid != null) {
                    // 채팅방 존재 시 메시지 보냄
                    sendMessage(chatRoomUid.toString(), _message)
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
    fun createChatRoom() {
        Log.d(TAG, "########### createChatRoom() ###########")

        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        val chatRoom = ChatRoom()
        chatRoom.users[uid.toString()] = ChatUser(true, curTime)
        chatRoom.users[_opponent] = ChatUser(true, curTime)

        val chatRoomUid: String? = chatRef.child("chatRooms").push().key
        chatRef.child("chatRooms").child(chatRoomUid.toString()).setValue(chatRoom)

        // 채팅방 생성 후 메시지 보냄
        sendMessage(chatRoomUid.toString(), _message)
    }

    // 채팅 불러오기
    fun loadChat(roomId: String) {
        Log.d(TAG, "########### loadChat(${roomId}) ###########")

        chatRef.child("chatRooms").child(roomId)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataList = ArrayList<Chat>()

                    for(chat in snapshot.child("messages").children) {
                        chat.getValue<Chat>()?.let { dataList.add(it) }
                    }
                    chatList.value = dataList
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}