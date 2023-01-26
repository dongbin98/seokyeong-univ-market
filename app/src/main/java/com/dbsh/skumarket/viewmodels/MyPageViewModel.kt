package com.dbsh.skumarket.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MyPageViewModel: ViewModel() {
    var isSaved: MutableLiveData<String> = MutableLiveData()
    var myProfile: MutableLiveData<Uri?> = MutableLiveData()
    private val auth by lazy { Firebase.auth }
    private val storage by lazy { Firebase.storage }
    private val uid = auth.currentUser?.uid
    private val profileRef = storage.reference.child("profile")

    fun loadProfileImage() {
        val myProfileRef = profileRef.child(uid.toString())
        if(myProfileRef == null) {
            myProfile.value = null
        } else {
            myProfileRef.child("profile").downloadUrl.addOnSuccessListener {
                myProfile.value = it
            }.addOnFailureListener {
                myProfile.value = null
            }
        }
    }

    fun saveProfile(image: Uri) {
        profileRef.child(uid.toString()).child("profile").putFile(image).addOnSuccessListener {
            isSaved.value = "S"
        }.addOnFailureListener {
            isSaved.value = "F"
        }
    }
}