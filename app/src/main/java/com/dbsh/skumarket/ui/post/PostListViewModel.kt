package com.dbsh.skumarket.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.api.model.PostList
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostListViewModel: ViewModel() {

    private var _loadLiveData = MutableLiveData<Resource<ArrayList<PostList>>>()
    var loadLiveData: LiveData<Resource<ArrayList<PostList>>> = _loadLiveData

    private val repository = FirebaseRepository()

    fun loadPosts() {
        _loadLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val list = repository.loadPosts()
            _loadLiveData.postValue(list)
        }
    }
}