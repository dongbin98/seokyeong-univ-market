package com.dbsh.skumarket.ui.post

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.ImageAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityUploadPostBinding
import com.dbsh.skumarket.ui.main.MainActivity
import com.dbsh.skumarket.util.Resource

class UploadPostActivity : BaseActivity<ActivityUploadPostBinding>(R.layout.activity_upload_post) {

    companion object {
        const val REQUEST_EXTERNAL_STORAGE = 100
    }

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageList: MutableList<Uri>
    lateinit var viewModel: UploadPostViewModel

    @SuppressLint("SetTextI18n")
    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList.reversed())
            binding.uploadPostImageCount.text = "${imageAdapter.itemCount}/10"
        }

    override fun init() {
        viewModel = UploadPostViewModel()
        binding.apply {
            viewModel = viewModel
        }

        setSupportActionBar(binding.uploadPostToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        initRecyclerView()

        binding.uploadPostPhoto.setOnClickListener {
            checkPermission() // Api Level 33 처리 예정
//            loadImage()
        }

        binding.uploadPostButton.setOnClickListener {
            if (imageList.isNotEmpty() && binding.uploadPostTitle.text.isNotBlank() && binding.uploadPostPrice.text.isNotBlank() && binding.uploadPostContent.text.isNotBlank()) {
                viewModel.upload(
                    binding.uploadPostTitle.text.toString(),
                    binding.uploadPostPrice.text.toString(),
                    binding.uploadPostContent.text.toString(),
                    imageList
                )
            } else {
                Toast.makeText(this, "모든 내용은 필수 기재입니다", Toast.LENGTH_SHORT).show()
            }
            println("!check :: ${imageAdapter.itemCount}")
        }

        viewModel.uploadLiveData.observe(this) {
            when(it) {
                is Resource.Loading -> {
                    Toast.makeText(this, "업로드중", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
                    setResult(100, intent)
                    finish()
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
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

    private fun updateImages(uriList: List<Uri>) {
//        val images = uriList.map { ImageItems.Image(it) }
        imageList.addAll(uriList)
        imageAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        imageList = mutableListOf()
        imageAdapter = ImageAdapter(imageList, object : ImageAdapter.ItemClickListener {
            override fun deleteImage(position: Int) {
                println("Clicked position = $position")
                imageList.removeAt(position)
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