package com.dbsh.skumarket.ui.post

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ImageAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityUploadPostBinding
import com.dbsh.skumarket.util.Resource
import kotlinx.coroutines.*

class UploadPostActivity : BaseActivity<ActivityUploadPostBinding>(R.layout.activity_upload_post) {

    companion object {
        const val REQUEST_EXTERNAL_STORAGE = 100
    }

    // 업데이트 시 필요한 기존 정보
    private lateinit var postId: String
    private lateinit var uid: String
    private lateinit var time: String
    private var isUpdate = false


    private lateinit var imageAdapter: ImageAdapter
    private lateinit var uriList: MutableList<Uri>
    private val bitmapList: MutableList<Bitmap> by lazy { mutableListOf() }
    lateinit var viewModel: UploadPostViewModel

    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList.reversed())
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun init() {
        viewModel = UploadPostViewModel()
        binding.apply {
            viewModel = viewModel
        }
        initRecyclerView()

        if (!intent.getStringExtra("uid").isNullOrEmpty() && !intent.getStringExtra("postId")
                .isNullOrEmpty()
        ) {
            uid = intent.getStringExtra("uid").toString()
            postId = intent.getStringExtra("postId").toString()
            Log.d("uid", uid)
            Log.d("postId", postId)
            // 수정을 통해 해당 액티비티에 온 경우
            viewModel.loadPost(postId)
            binding.uploadPostButton.text = "수정하기"
            isUpdate = true
        }

        setSupportActionBar(binding.uploadPostToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.uploadPostPhoto.setOnClickListener {
            checkPermission() // Api Level 33 처리 예정
//            loadImage()
        }

        binding.uploadPostButton.setOnClickListener {
            if (uriList.isNotEmpty() && binding.uploadPostTitle.text.isNotBlank() && binding.uploadPostPrice.text.isNotBlank() && binding.uploadPostContent.text.isNotBlank()) {
                if (isUpdate) { // 게시글 업데이트
                    uriList.forEach {
                        val bitmap = ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                it
                            )
                        )
                        println(bitmap)
                        bitmapList.add(bitmap)
                        if (bitmapList.size == uriList.size) {
                            viewModel.update(
                                postId,
                                time,
                                binding.uploadPostTitle.text.toString(),
                                binding.uploadPostPrice.text.toString(),
                                binding.uploadPostContent.text.toString(),
                                bitmapList
                            )
                            bitmapList.clear()
                        }
                    }
                } else {    // 게시글 새 등록
                    viewModel.upload(
                        binding.uploadPostTitle.text.toString(),
                        binding.uploadPostPrice.text.toString(),
                        binding.uploadPostContent.text.toString(),
                        uriList
                    )
                }
            } else {
                Toast.makeText(this, "모든 내용은 필수 기재입니다", Toast.LENGTH_SHORT).show()
            }
            println("!check :: ${imageAdapter.itemCount}")
        }

        viewModel.uploadLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Toast.makeText(this, "업로드중", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
                    setResult(100, intent)
                    finish()    // PostListFragment 로 돌아감
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.updatePostLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Toast.makeText(this, "업데이트 중", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Toast.makeText(this, "업데이트 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("postId", it.data.toString())
                    setResult(100, intent)
                    finish()    // PostDetailActivity 로 돌아가서 해당 postId로 갱신시키기 위함
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loadPostLiveData.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("loadPostLiveData", "게시글 로드중")
                }
                is Resource.Success -> {
                    Log.d("loadPostLiveData", "게시글 로드완료")
                    binding.uploadPostTitle.setText(result.data!!.title)
                    binding.uploadPostPrice.setText(result.data.price)
                    binding.uploadPostContent.setText(result.data.content)
                    time = result.data.time
                    updateImages(result.data.images.values.map { it.toUri() }.reversed())
                }
                is Resource.Error -> {
                    Log.d("loadPostLiveData", result.message.toString())
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun requestReadExternalStorage() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE
        )
    }

    private fun loadImage() {
        imageLoadLauncher.launch("image/*")
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun updateImages(_uriList: List<Uri>) {
//        val images = uriList.map { ImageItems.Image(it) }
        uriList.addAll(_uriList)
        imageAdapter.notifyDataSetChanged()
        binding.uploadPostImageCount.text = "${imageAdapter.itemCount}/10"
    }

    private fun initRecyclerView() {
        uriList = mutableListOf()
        imageAdapter = ImageAdapter(uriList, object : ImageAdapter.ItemClickListener {
            override fun deleteImage(position: Int) {
                println("Clicked position = $position")
                uriList.removeAt(position)
                imageAdapter.notifyItemRemoved(position)
                binding.uploadPostImageCount.text = "${imageAdapter.itemCount}/10"
            }
        })
        binding.uploadPostImageList.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }
}