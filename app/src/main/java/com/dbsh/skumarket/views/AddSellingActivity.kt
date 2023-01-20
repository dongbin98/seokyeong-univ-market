package com.dbsh.skumarket.views

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.dbsh.skumarket.R
import com.dbsh.skumarket.model.SellingModelData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddSellingActivity : AppCompatActivity() {
    private var selectedUri: Uri? = null

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val sellingDB: DatabaseReference by lazy {
        Firebase.database.reference.child("Selling")
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri->

        if(uri != null) {
            // 사진을 정상적으로 가져왔을때
            findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
            selectedUri = uri
        } else {
            Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_selling)

        // 이미지 추가 버튼
        findViewById<Button>(R.id.imageAddButton).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> { // 권한을 가지고 있는 경우;
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 팝업 확인창 띄우기
                    showPermissionContextPop()
                }
                else -> {
                    // 권한 요청
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1010
                    )
                }
            }

        }
        // 게시글 등록 버튼
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            showProgress()
            // 입력된 값 가져오기
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val price = findViewById<EditText>(R.id.priceEditText).text.toString()
            val contents = findViewById<EditText>(R.id.contentEditText).text.toString()
            val uId = auth.currentUser?.uid.orEmpty()

            // 업로드중 이미지가 있으면 업로드 과정을 추가함
            if (selectedUri != null) {
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = { url -> // 다운로드 url을 받아서 처리
                        uploadSelling(uId, title, price, contents, url)
                    },
                    errorHandler = {
                        Toast.makeText(this, "게시글 업로드 실패", Toast.LENGTH_SHORT).show()
                        hideProgress()
                    })
            } else {
                //이미지가 없는 경우 빈 문자열로 남김
                uploadSelling(uId, title, price, contents, "")
                hideProgress()
            }
            // 모델 생성
        }
    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("selling").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) { // 업로드 과정 완료
                    // 다운로드 url 가져오기
                    storage.reference.child("selling").child(fileName).downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    Log.d("yang", it.exception.toString())
                    errorHandler()
                }
            }
    }

    private fun uploadSelling(uId: String, title: String, price: String, contents: String, imageUrl: String) {
        val model = SellingModelData(uId, title, System.currentTimeMillis(), "${price}원", contents, imageUrl)

        // 데이터베이스에 업로드
        sellingDB.push().setValue(model)

        hideProgress()
        finish()
    }

    // 권힌 요청 결과 확인;
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1010 -> {
                // 권한을 허용한 경우;
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else { // 권한을 거부한 경우;
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun startContentProvider() {
        // 이미지 SAF 기능 실행; 이미지 가져오기;
        // startActivityForResult(intent, 2020) // deprecated
        // new ver.
        getContent.launch("image/*")
    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = true
        blockLayoutTouch()
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = false
        clearBlockLayoutTouch()
    }
    // 화면 터치 막기
    private fun blockLayoutTouch() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    // 화면 터치 풀기
    private fun clearBlockLayoutTouch() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // 팝업 메시지 출력 (권한 혀용)
    private fun showPermissionContextPop() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
            }
            .create()
            .show()
    }

}