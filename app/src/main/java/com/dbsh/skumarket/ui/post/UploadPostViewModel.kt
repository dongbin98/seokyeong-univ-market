package com.dbsh.skumarket.ui.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.api.model.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UploadPostViewModel: ViewModel() {

    private val uid by lazy { Firebase.auth.uid }
    private val storage by lazy { Firebase.storage.reference.child("Post") }
    private val database by lazy { Firebase.database.reference.child("Post") }
    private val dateFormat = SimpleDateFormat("MM월 DD일")
    private var uriList = mutableMapOf<Int, String>()

    fun upload(title: String, price: String, content: String, imageUrl: MutableList<Uri>) {
        val time = System.currentTimeMillis()
        val postId = uid + time

        uploadPhoto(postId, imageUrl)
        uploadPost(postId, title, price, content)
    }

    private fun uploadPhoto(postId: String, imageUri: List<Uri>) {
        uriList.clear()
        var itemCount = 1
        imageUri.forEach { uri ->
            val fileName = "${System.currentTimeMillis()}.png"
            storage.child(postId).child(fileName).putFile(uri).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    storage.child(postId).child(fileName).downloadUrl.addOnSuccessListener {
                        uriList[itemCount] = it.toString()
                    }
                }
            }
            itemCount++
        }
    }

    private fun uploadPost(postId: String, title: String, price: String, content: String) {
        val time = System.currentTimeMillis()
        val curTime = dateFormat.format(Date(time)).toString()
        val post = Post(uid.toString(), title, curTime, price, content, uriList as HashMap)
        database.child("posts").child(postId).setValue(post).addOnSuccessListener {

        }
    }
}