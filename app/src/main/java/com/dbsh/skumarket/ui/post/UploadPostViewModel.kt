package com.dbsh.skumarket.ui.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.api.model.Post
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import com.dbsh.skumarket.util.currentDate
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UploadPostViewModel : ViewModel() {
    private val uid by lazy { Firebase.auth.uid }

    private val repository = FirebaseRepository()
    private var _uploadLiveData = MutableLiveData<Resource<Task<Void>>>()
    var uploadLiveData: LiveData<Resource<Task<Void>>> = _uploadLiveData

    fun upload(title: String, price: String, content: String, imageUrl: MutableList<Uri>) {
        viewModelScope.launch {
            _uploadLiveData.postValue(Resource.Loading())
            val postId = uid + currentDate()
            repository.uploadPhoto(postId, imageUrl)
            val result = repository.uploadPost(postId, title, price, content)
            _uploadLiveData.postValue(result)
        }
    }
}