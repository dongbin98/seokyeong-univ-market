package com.dbsh.skumarket.repository

import android.net.Uri
import com.dbsh.skumarket.api.model.Post
import com.dbsh.skumarket.api.model.User
import com.dbsh.skumarket.util.Resource
import com.dbsh.skumarket.util.safeCall
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FirebaseRepository {
    private val firebaseAuth = Firebase.auth
    private val storageReference = Firebase.storage.reference
    private val databaseReference = Firebase.database.reference
    private val dateFormat = SimpleDateFormat("MM월 DD일")
    private var uriList = mutableMapOf<Int, String>()

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

    suspend fun login(email: String, pw: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, pw).await()
                Resource.Success(result)
            }
        }
    }

    suspend fun uploadPhoto(postId: String, imageUri: List<Uri>) {
        return withContext(Dispatchers.IO) {
            val storage = storageReference.child("Post")
            uriList.clear()
            var itemCount = 0
            imageUri.forEach {
                val fileName = "${System.currentTimeMillis()}.png"

                storage.child(postId).child(fileName).putFile(it).addOnCompleteListener { task ->
                    val uri: Task<Uri> = task.result.storage.downloadUrl
                    while(!uri.isComplete) {
                        Thread.sleep(1)
                    }
                    uriList[itemCount] = uri.result.toString()
                }
            }
            itemCount++
        }
    }

    suspend fun uploadPost(postId: String, title: String, price: String, content: String): Resource<Task<Void>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val database = databaseReference.child("Post")
                val time = System.currentTimeMillis()
                val curTime = dateFormat.format(Date(time)).toString()
                val post = Post(firebaseAuth.uid.toString(), title, curTime, price, content, uriList as HashMap)

                Resource.Success(database.child("posts").child(postId).setValue(post).addOnCompleteListener {
                    Resource.Success(it)
                })
            }
        }
    }

    suspend fun loadPost(postId: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                Resource.Success(true)
            }
        }
    }

    suspend fun loadPosts(): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val database = databaseReference.child("Post").child("posts")
                val storage = storageReference.child("Post")
                val snapshot = database.get().await()
                snapshot.children.forEach {
                    it.child("uid").value.toString()
                }

                Resource.Success(true)
            }
        }
    }
}