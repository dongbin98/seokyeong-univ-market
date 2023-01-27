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
    var chatList: MutableLiveData<ArrayList<ChatListDto>> = MutableLiveData()
    var deleteSignal: MutableLiveData<String> = MutableLiveData();
    private val auth by lazy { Firebase.auth }
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private val userRef = Firebase.database.reference.child("User")
    private val uid = auth.currentUser?.uid

    fun loadChatRoom() {
        Log.d(ContentValues.TAG, "########### loadChatRoom() ###########")

        val dataList = ArrayList<ChatListDto>()
        var noData = true;
        chatRef.child("chatRooms").orderByChild("users/$uid/join").equalTo(true)
            .addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                noData = false;
                for(chatRoom in snapshot.children) {
                    println("chatRoom = $chatRoom")

                    var lastChat: ChatListDto? = null
                    val chatRoomKey = chatRoom.key.toString()
                    var opponentName: String?
                    var opponentImage: String?
                    var lastMessage = ""
                    var lastChatTime = ""

                    for(user in chatRoom.child("users").children) {
                        if(user.key.toString() != uid) {
                            userRef.child("users").child(user.key.toString()).addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    opponentName = snapshot.child("name").value.toString()
                                    opponentImage = snapshot.child("profileImage").value.toString().ifBlank { null }

                                    for(chat in chatRoom.child("messages").children) {
                                        lastMessage = chat.child("message").value.toString().ifBlank { "사진을 업로드했습니다" }
                                        lastChatTime = chat.child("time").value.toString()
                                    }
                                    lastChat = ChatListDto(chatRoomKey, lastMessage, lastChatTime, opponentName!!, opponentImage)
                                    dataList.add(lastChat!!)
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
        if(noData) {
            chatList.value = dataList
        }
    }

    // 채팅방 나가기
    fun deleteChatRoom(roomId: String) {
        Log.d(ContentValues.TAG, "########### deleteChatRoom(${roomId}) ###########")
        val map = HashMap<String, Any>();
        map[uid.toString()] = ChatUser(false, null)
        chatRef.child("chatRooms").child(roomId).child("users").updateChildren(map).addOnSuccessListener {
            println("삭제완료")
            deleteSignal.value = "S";
        }
    }
}