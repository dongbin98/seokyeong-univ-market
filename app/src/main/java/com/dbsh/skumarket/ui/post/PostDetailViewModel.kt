package com.dbsh.skumarket.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.api.model.Post
import com.dbsh.skumarket.api.model.User
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {
    private var _loadPostLiveData = MutableLiveData<Resource<Post>>()
    var loadPostLiveData: LiveData<Resource<Post>> = _loadPostLiveData

    private var _loadProfileLiveData = MutableLiveData<Resource<User>>()
    var loadProfileLiveData: LiveData<Resource<User>> = _loadProfileLiveData

    private val repository = FirebaseRepository()

    fun loadPost(postId: String, uid: String) {
        _loadPostLiveData.postValue(Resource.Loading())
        _loadProfileLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val loadPostResult = repository.loadPost(postId)
            val loadProfileResult = repository.loadProfile(uid)
            _loadPostLiveData.postValue(loadPostResult)
            _loadProfileLiveData.postValue(loadProfileResult)
        }
    }
}