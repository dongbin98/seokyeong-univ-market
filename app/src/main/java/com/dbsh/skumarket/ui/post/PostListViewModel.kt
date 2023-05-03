package com.dbsh.skumarket.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostListViewModel: ViewModel() {
    private val repository = FirebaseRepository()

    fun loadPosts() {
        viewModelScope.launch(Dispatchers.Main) {
            repository.loadPosts()
        }
    }
}