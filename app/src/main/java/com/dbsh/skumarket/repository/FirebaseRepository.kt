package com.dbsh.skumarket.repository

import com.dbsh.skumarket.api.model.User
import com.dbsh.skumarket.util.Resource
import com.dbsh.skumarket.util.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {
    private val firebaseAuth = Firebase.auth
    private val databaseReference = Firebase.database.reference

    suspend fun checkRegister(stuId: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                var isChecked = true
                val snapshot = databaseReference.child("User").child("users").get().await()
                snapshot.children.forEach {
                    if(it.child("uid").value.toString() == stuId) {
                        println("check : ${it.child("uid").value.toString()}")
                        isChecked = false
                    }
                }
                if(!isChecked)
                    Resource.Error("Already registered student", null)
                else
                    Resource.Success(true)
            }
        }
    }

    suspend fun createUser(email: String, pw: String, name: String, stuId: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val createResult = firebaseAuth.createUserWithEmailAndPassword(email, pw).await()
                val userId = createResult.user?.uid
                val newUser = User(name, stuId)
                databaseReference.child("User").child("users").child(userId.toString()).setValue(newUser).await()
                Resource.Success(createResult)
            }
        }
    }

    suspend fun login(email: String, pw: String) {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, pw).await()
                Resource.Success(result)
            }
        }
    }
}