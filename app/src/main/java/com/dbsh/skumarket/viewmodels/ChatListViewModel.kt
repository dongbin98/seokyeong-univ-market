package com.dbsh.skumarket.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.model.ChatListDto
import com.dbsh.skumarket.model.ChatUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatListViewModel : ViewModel() {
    var protoChatList: MutableLiveData<ArrayList<ChatListDto>> = MutableLiveData()
    var chatList: MutableLiveData<ArrayList<ChatListDto>> = MutableLiveData()
    var deleteSignal: MutableLiveData<String> = MutableLiveData()
    private val auth by lazy { Firebase.auth }
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private val userRef = Firebase.database.reference.child("User")
    private val uid = auth.currentUser?.uid

    fun loadChatRoom() {
        Log.d(ContentValues.TAG, "########### loadChatRoom() ###########")

        val dataList = ArrayList<ChatListDto>()
        var noData = true
        chatRef.child("chatRooms").orderByChild("users/$uid/join").equalTo(true).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "chatRef.child(\"chatRooms\").orderByChild(\"users/$uid/join\").equalTo(true).addValueEventListener")
                dataList.clear()
                var lastChat: ChatListDto? = null
                noData = false

                // 내가 포함된 채팅방에서 채팅방 하나하나 개별 조회
                for (chatRoom in snapshot.children) {
                    println("chatRoom = $chatRoom")

                    val chatRoomKey = chatRoom.key.toString()
                    var chatRoomOtherId = ""
                    for (user in chatRoom.child("users").children) {
                        if (user.key.toString() != uid.toString()) {
                            chatRoomOtherId = user.key.toString()
                        }
                    }
                    var lastMessage = ""
                    var lastChatTime = ""

                    for (chat in chatRoom.child("messages").children) {
                        lastMessage = chat.child("message").value.toString().ifBlank { "사진을 업로드했습니다" }
                        lastChatTime = chat.child("time").value.toString()
                    }
                    lastChat = ChatListDto(chatRoomKey, lastMessage, lastChatTime, "", chatRoomOtherId, "")
                    dataList.add(lastChat)
                    println("dataList add")
                }
                protoChatList.value = dataList
            }
        })
        if (noData) {
            protoChatList.value = dataList
        }
    }

    fun loadUserProfile(dataList: ArrayList<ChatListDto>) {
        for ((position, data) in dataList.withIndex()) {
            val opponentId = data.opponentId
            userRef.child("users").child(opponentId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(ContentValues.TAG, "userRef.child(\"users\").child(user.key.toString()).addValueEventListener")
                    val opponentName = snapshot.child("name").value.toString()
                    val opponentImage = snapshot.child("profileImage").value.toString().ifBlank { null }

                    println("id : $opponentId 님의 이름은 $opponentName")

                    dataList[position].opponentName = opponentName
                    dataList[position].opponentImage = opponentImage
                    println("dataList set")
                    chatList.value = dataList
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    // 채팅방 나가기
    fun deleteChatRoom(roomId: String) {
        Log.d(ContentValues.TAG, "########### deleteChatRoom($roomId) ###########")
        val map = HashMap<String, Any>()
        map[uid.toString()] = ChatUser(false, "out")
        chatRef.child("chatRooms").child(roomId).child("users").updateChildren(map).addOnSuccessListener {
            println("삭제완료")
            deleteSignal.value = "S"
        }
    }
}
