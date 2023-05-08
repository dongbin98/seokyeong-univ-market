package com.dbsh.skumarket.ui.post

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.api.model.Post
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import com.dbsh.skumarket.util.currentDate
import com.dbsh.skumarket.util.dateToString
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

    private var _loadPostLiveData = MutableLiveData<Resource<Post>>()
    var loadPostLiveData: LiveData<Resource<Post>> = _loadPostLiveData

    private var _updatePostLiveData = MutableLiveData<Resource<String>>()
    var updatePostLiveData: LiveData<Resource<String>> = _updatePostLiveData

    // Post 객체 전달 방식 업로드
    fun upload(title: String, price: String, content: String, uriList: MutableList<Uri>) {
        _uploadLiveData.postValue(Resource.Loading())
        viewModelScope.launch {
            val postId = uid + currentDate()
            val curTime = currentDate().dateToString("M월 d일 HH:mm")

            val imageMap = repository.uploadPhoto(postId, uriList) // 이미지 등록

            val post = Post(uid.toString(), title, curTime, price, content, imageMap)   // setValue
            val result = repository.uploadPost(uid + currentDate(), post)
            _uploadLiveData.postValue(result)
        }
    }

    fun update(postId: String, time: String, title: String, price: String, content: String, bitmapList: MutableList<Bitmap>) {
        _updatePostLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val newPostId = uid + currentDate()
            val imageMap = repository.uploadPhoto(newPostId, bitmapList) // 새 이미지 등록
            Log.d("update()", "새 이미지 등록 완료")

            val post = Post(uid.toString(), title, time, price, content, imageMap)
            repository.uploadPost(newPostId, post)   // updateChildren -> setValue 로 변경
            Log.d("update()", "새 게시글 등록 완료")

//            repository.deletePost(postId)   // 기존 게시글 및 이미지 삭제
            _updatePostLiveData.postValue(Resource.Success(newPostId))
        }
    }

    fun loadPost(postId: String) {
        Log.d("loadPost", "업데이트 화면 띄우기")
        _loadPostLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val loadPostResult = repository.loadPost(postId)
            _loadPostLiveData.postValue(loadPostResult)
        }
    }
}