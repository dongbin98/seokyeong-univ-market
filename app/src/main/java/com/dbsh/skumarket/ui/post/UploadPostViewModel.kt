package com.dbsh.skumarket.ui.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.api.model.Post
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UploadPostViewModel: ViewModel() {

    private val uid by lazy { Firebase.auth.uid }
//    private val storage by lazy { Firebase.storage.reference.child("Post") }
//    private val database by lazy { Firebase.database.reference.child("Post") }
//    private val dateFormat = SimpleDateFormat("MM월 DD일")
    private val repository = FirebaseRepository()
//    private var uriList = mutableMapOf<Int, String>()


//    private var _uploadLiveData = MutableLiveData<Boolean>()
//    var uploadLiveData: LiveData<Boolean> = _uploadLiveData
    private var _uploadLiveData = MutableLiveData<Resource<Task<Void>>>()
    var uploadLiveData: LiveData<Resource<Task<Void>>> = _uploadLiveData

    fun upload(title: String, price: String, content: String, imageUrl: MutableList<Uri>) {
        viewModelScope.launch {
            _uploadLiveData.postValue(Resource.Loading())
            val time = System.currentTimeMillis()
            val postId = uid + time
//            uploadPhoto(postId, imageUrl)
//            uploadPost(postId, title, price, content)
            repository.uploadPhoto(postId, imageUrl)
            val result = repository.uploadPost(postId, title, price, content)
            _uploadLiveData.postValue(result)
        }
    }

/*    private fun uploadPhoto(postId: String, imageUri: List<Uri>) {
        uriList.clear()
        var itemCount = 0
        imageUri.forEach {
            val fileName = "${System.currentTimeMillis()}.png"
            storage.child(postId).child(fileName).putFile(it).addOnCompleteListener { task ->
//                storage.child(postId).child(fileName).downloadUrl.addOnSuccessListener {
//                uriList[itemCount] = it.toString()
//                    }
                val uri: Task<Uri> = task.result.storage.downloadUrl
                while(!uri.isComplete) {
                    Thread.sleep(1)
                }
                uriList[itemCount] = uri.result.toString()
            }
        }
        itemCount++
    }

    private fun uploadPost(postId: String, title: String, price: String, content: String) {
        val time = System.currentTimeMillis()
        val curTime = dateFormat.format(Date(time)).toString()
        val post = Post(uid.toString(), title, curTime, price, content, uriList as HashMap)
        database.child("posts").child(postId).setValue(post).addOnSuccessListener {
            _uploadLiveData.postValue(true)
        }.addOnFailureListener {
            _uploadLiveData.postValue(false)
        }
    }*/
}