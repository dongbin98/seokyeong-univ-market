package com.dbsh.skumarket.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel: ViewModel() {
    var loginState: MutableLiveData<String> = MutableLiveData()
    val loginUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun login(id: String, pw: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(id, pw).addOnCompleteListener {
            if(it.isSuccessful) {
                loginState.value = "S"
                loginUser.value = auth.currentUser
            } else {
                loginState.value = "F"
            }
        }
    }
}