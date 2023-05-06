package com.dbsh.skumarket.repository

import android.annotation.SuppressLint
import android.net.Uri
import com.dbsh.skumarket.api.model.Post
import com.dbsh.skumarket.api.model.PostList
import com.dbsh.skumarket.api.model.User
import com.dbsh.skumarket.util.Resource
import com.dbsh.skumarket.util.currentDate
import com.dbsh.skumarket.util.dateToString
import com.dbsh.skumarket.util.safeCall
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FirebaseRepository {
    private val firebaseAuth = Firebase.auth
    private val storageReference = Firebase.storage.reference
    private val databaseReference = Firebase.database.reference
    private var uriList = mutableMapOf<String, String>()

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

            // 이미지 업로드
            for((index, uri) in imageUri.withIndex()) {
                if(index >= 10) // 10회 제한
                    break
                val fileName = "${System.currentTimeMillis()}.png"
                val task = storage.child(postId).child(fileName).putFile(uri).await()
                task.storage.downloadUrl.addOnCompleteListener {
                    println("url:${it.result}")
                    uriList[index.toString()] = it.result.toString()
                }.await()
            }
        }
    }

    suspend fun uploadPost(postId: String, title: String, price: String, content: String): Resource<Task<Void>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val database = databaseReference.child("Post")
                val curTime = currentDate().dateToString("M월 d일 HH:mm")
                val post = Post(firebaseAuth.uid.toString(), title, curTime, price, content, uriList as HashMap)
                Resource.Success(database.child("posts").child(postId).setValue(post).addOnCompleteListener {
                    Resource.Success(it)
                })
            }
        }
    }

    suspend fun deletePost(postId: String): Resource<Task<Void>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val database = databaseReference.child("Post").child("posts").child(postId)

                // Firebase Storage 폴더 삭제 미지원 -> 재귀삭제
                database.child("images").get().addOnCompleteListener {
                    it.result.children.forEach { snapshot ->
                        storageReference.storage.getReferenceFromUrl(snapshot.value.toString()).delete()
                    }
                }.await()

                // 게시글 삭제
                Resource.Success(database.removeValue().addOnCompleteListener {
                    Resource.Success(it)
                })
            }
        }
    }

    suspend fun loadProfile(uid: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val database = databaseReference.child("User").child("users").child(uid)
                val user = User()
                database.get().addOnCompleteListener { snapshot ->
                    user.uid = snapshot.result.child("uid").value.toString()
                    user.name = snapshot.result.child("name").value.toString()
                    user.profileImage = snapshot.result.child("profileImage").value.toString()
                    println(user.toString())
                }.await()
                Resource.Success(user)
            }
        }
    }

    suspend fun loadPost(postId: String): Resource<Post> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val database = databaseReference.child("Post").child("posts").child(postId)
                val post = Post()
                database.get().addOnCompleteListener { snapshot ->
                    val map = hashMapOf<String, String>()
                    snapshot.result.child("images").children.forEach {
                        map[it.key.toString()] = it.value.toString()
                    }
                    post.uid = snapshot.result.child("uid").value.toString()
                    post.title = snapshot.result.child("title").value.toString()
                    post.time = snapshot.result.child("time").value.toString()
                    post.price = snapshot.result.child("price").value.toString()
                    post.content = snapshot.result.child("content").value.toString()
                    post.images = map
                    println(post.toString())
                }.await()
                Resource.Success(post)
            }
        }
    }

    suspend fun loadPosts(): Resource<ArrayList<PostList>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val list = mutableListOf<PostList>()
                val database = databaseReference.child("Post").child("posts")

                val snapshot = database.get().await()
                snapshot.children.forEach {
                    val post = PostList(
                        it.key.toString(),
                        it.child("images").child("0").value.toString(),
                        it.child("title").value.toString(),
                        it.child("time").value.toString(),
                        it.child("price").value.toString(),
                        it.child("uid").value.toString()
                    )
                    list.add(post)
                }
                Resource.Success(list as ArrayList<PostList>)
            }
        }
    }
}