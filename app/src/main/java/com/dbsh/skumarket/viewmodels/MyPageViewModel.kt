package com.dbsh.skumarket.viewmodels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.model.Chat
import com.dbsh.skumarket.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MyPageViewModel: ViewModel() {
    var isSaved: MutableLiveData<String> = MutableLiveData()
    var myProfile: MutableLiveData<String> = MutableLiveData()
    private val auth by lazy { Firebase.auth }
    private val storage by lazy { Firebase.storage }
    private val uid = auth.currentUser?.uid
    private val userRef = Firebase.database.reference.child("User")
    private val storageRef = storage.reference.child("profile")

    fun loadProfileImage() {
        userRef.child("users").child(uid.toString()).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "userRef.child(\"users\").child(uid.toString()).addListenerForSingleValueEvent")
                if (snapshot.child("profileImage").value.toString() == "")
                    myProfile.value = ""
                else
                    myProfile.value = snapshot.child("profileImage").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                myProfile.value = ""
            }
        })
    }

    fun saveProfile(image: Uri) {
        // 이미지 업로드 시도
        storageRef.child(uid.toString()).child("profile").putFile(image).addOnSuccessListener {
            // 업로드 성공 시 해당 이미지 다운로드
            storageRef.child(uid.toString()).child("profile").downloadUrl.addOnSuccessListener {
                // 다운받은 Uri 데이터베이스에 저장
                userRef.child("users").child(uid.toString()).addListenerForSingleValueEvent(object:
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(ContentValues.TAG, "userRef.child(\"users\").child(uid.toString()).addListenerForSingleValueEvent")
                        val map: HashMap<String, Any> = HashMap()
                        map["profileImage"] = it.toString()
                        userRef.child("users").child(uid.toString()).updateChildren(map)
                        isSaved.value = "S"
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }.addOnFailureListener {
            isSaved.value = "F"
        }
    }
}