package com.dbsh.skumarket.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var _loginState = MutableLiveData<Resource<AuthResult>>()
    var loginState: LiveData<Resource<AuthResult>> = _loginState

    private val repository = FirebaseRepository()

    fun login(email: String, pw: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _loginState.postValue(Resource.Loading())
            val authResult = repository.login(email, pw)
            _loginState.postValue(authResult)
        }
    }
}
