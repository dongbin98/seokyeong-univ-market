package com.dbsh.skumarket.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    var loginState: MutableLiveData<String> = MutableLiveData()
    var loginUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun login(email: String, pw: String) {
        val auth = Firebase.auth
        println(email)
        println(pw)
        auth.signInWithEmailAndPassword(email, pw).addOnCompleteListener {
            if (it.isSuccessful) {
                loginUser.value = auth.currentUser
                loginState.value = "S"
            } else {
                loginState.value = "F"
            }
        }
    }
}
