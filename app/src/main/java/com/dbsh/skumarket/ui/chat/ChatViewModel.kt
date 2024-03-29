package com.dbsh.skumarket.ui.chat

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.api.model.Chat
import com.dbsh.skumarket.api.model.ChatRoom
import com.dbsh.skumarket.api.model.ChatUser
import com.dbsh.skumarket.api.model.User
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

class ChatViewModel : ViewModel() {
    var chatList: MutableLiveData<ArrayList<Chat>> = MutableLiveData()
    private val auth by lazy { Firebase.auth }
    private val userRef = Firebase.database.reference.child("User")
    private val chatRef = Firebase.database.reference.child("ChatRoom")
    private val storageRef = Firebase.storage.reference.child("ChatRoom")
    private val uid = auth.currentUser?.uid

    private var _roomId = MutableLiveData<String?>()
    val roomId: LiveData<String?> = _roomId

    // 메시지 보내기
    @SuppressLint("SimpleDateFormat")
    fun sendMessage(chatRoomId: String, message: String) {
        Log.d(TAG, "########### sendMessage($chatRoomId, $message) ###########")
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 HH:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        userRef.child("users").child(uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myName = snapshot.getValue<User>()?.name.toString()

                val chat = Chat(uid.toString(), message, "", curTime, myName)
                println("chatRoomUid = $chatRoomId")
                chatRef.child("chatRooms").child(chatRoomId).child("messages").push().setValue(chat)

                // 상대가 채팅방을 나간 경우 메시지 전송 시 다시 들어와지도록
                chatRef.child("chatRooms").child(chatRoomId).child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (user in snapshot.children) {
                            if (user.key != uid.toString() && user.child("join").value != true) {
                                val map = HashMap<String, Any>()
                                map[user.key.toString()] = ChatUser(true, curTime)
                                chatRef.child("chatRooms").child(chatRoomId).child("users").updateChildren(map).addOnSuccessListener {
                                }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 이미지 보내기
    @SuppressLint("SimpleDateFormat")
    fun sendImage(chatRoomId: String, image: Uri) {
        Log.d(TAG, "########### sendImage($chatRoomId) ###########")
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 HH:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        storageRef.child(chatRoomId).child(curTime).putFile(image).addOnSuccessListener {
            storageRef.child(chatRoomId).child(curTime).downloadUrl.addOnSuccessListener {
                userRef.child("users").child(uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val myName = snapshot.getValue<User>()?.name.toString()
                        val myImage = it.toString()
                        val chat = Chat(uid.toString(), "", myImage, curTime, myName)
                        chatRef.child("chatRooms").child(chatRoomId).child("messages").push().setValue(chat)

                        // 상대가 채팅방을 나간 경우 메시지 전송 시 다시 들어와지도록
                        chatRef.child("chatRooms").child(chatRoomId).child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (user in snapshot.children) {
                                    if (user.key != uid.toString() && user.child("join").value != true) {
                                        val map = HashMap<String, Any>()
                                        map[user.key.toString()] = ChatUser(true, curTime)
                                        chatRef.child("chatRooms").child(chatRoomId).child("users").updateChildren(map).addOnSuccessListener {
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    // 채팅방 유무 체크
    fun checkChatRoom(opponent: String) {
        Log.d(TAG, "########### checkChatRoom($opponent) ###########")

        var chatRoomUid: String? = null
        chatRef.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        if (item.getValue<ChatRoom>()?.users?.containsKey(opponent) == true) {
                            chatRoomUid = item.key
                        }
                    }
                    if (chatRoomUid != null) {
                        // 채팅방 존재 시 메시지 보냄
                        loadChat(chatRoomUid!!)
                    } else {
                        // 채팅방 미존재 시 채팅방 생성
                        createChatRoom(opponent)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    // 채팅방 생성
    @SuppressLint("SimpleDateFormat")
    fun createChatRoom(opponent: String) {
        Log.d(TAG, "########### createChatRoom() ###########")

        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 HH:mm:ss")
        val curTime = dateFormat.format(Date(time)).toString()

        val chatRoom = ChatRoom()
        chatRoom.users[uid.toString()] = ChatUser(true, curTime)
        chatRoom.users[opponent] = ChatUser(true, curTime)

        val chatRoomUid: String? = chatRef.child("chatRooms").push().key
        chatRef.child("chatRooms").child(chatRoomUid.toString()).setValue(chatRoom)
        _roomId.postValue(chatRoomUid)
    }

    // 채팅 불러오기
    @SuppressLint("SimpleDateFormat")
    fun loadChat(chatRoomId: String) {
        Log.d(TAG, "########### loadChat($chatRoomId) ###########")
        val dateFormat = SimpleDateFormat("MM월dd일 HH:mm:ss")

        chatRef.child("chatRooms").child(chatRoomId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = ArrayList<Chat>()
                val myTime = snapshot.child("users").child(uid.toString()).child("time").value.toString()
                println("\n$chatRoomId 입장시각 : $myTime\n")
                if(myTime == "out")
                    return

                for (chat in snapshot.child("messages").children) {
                    print("메시지 시각 : ${chat.child("time").value}\n입장한 시각 : ${myTime}의 비교\n")
                    if(chat.child("time").value.toString() != "out") {
                        if (Date(dateFormat.parse(chat.child("time").value.toString())!!.time) >= Date(dateFormat.parse(myTime)!!.time)) {
                            println("메시지 시각이 더 늦으니 보여줄게요")
                            dataList.add(chat.getValue<Chat>()!!)
                            //                            chat.getValue<Chat>()?.let {
                            //                                dataList.add(it)
                            //                            }
                        }
                    }
                }
                chatList.value = dataList
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
