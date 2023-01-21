package com.dbsh.skumarket.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.model.ChatListDto
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatListViewModel : ViewModel() {
    var chatList: MutableLiveData<ArrayList<ChatListDto>> = MutableLiveData()
    private val auth = Firebase.auth
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private val userRef = Firebase.database.reference.child("User")
    private val uid = auth.currentUser?.uid

    fun loadChatRoom() {
        Log.d(ContentValues.TAG, "########### loadChatRoom() ###########")

        val dataList = ArrayList<ChatListDto>()

        chatRef.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                chatList.value = dataList
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for(chatRoom in snapshot.children) {
                    println("chatRoom = $chatRoom")

                    var lastChat: ChatListDto? = null
                    val chatRoomKey = chatRoom.key.toString()
                    var opponentName: String?

                    for(user in chatRoom.child("users").children) {
                        if(user.key.toString() != uid) {
                            userRef.child("users").child(user.key.toString()).child("name").addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    opponentName = snapshot.value.toString()
                                    println("대화상대 : $opponentName")

                                    for(chat in chatRoom.child("messages").children) {
//                                        println("messasge in data = $chat")
                                        lastChat = ChatListDto(chatRoomKey, chat.child("message").value.toString(), chat.child("time").value.toString(), opponentName.toString())
                                    }

                                    if (lastChat != null) {
                                        dataList.add(lastChat!!)
                                    }
                                    chatList.value = dataList
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                        }
                    }
                }
            }
        })
    }

    // 채팅방 나가기
    fun deleteChatRoom(roomId: String) {
        Log.d(ContentValues.TAG, "########### deleteChatRoom(${roomId}) ###########")
        val map = HashMap<String, Boolean>();
        map[uid.toString()] = false
        chatRef.child("chatRooms").child(roomId).child("users").updateChildren(map as Map<String, Any>).addOnCompleteListener {
            loadChatRoom()
        }
    }
}