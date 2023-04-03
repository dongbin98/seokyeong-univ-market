package com.dbsh.skumarket.ui.mypage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.databinding.FragmentMyPageBinding

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    companion object {
        const val TAG = "MyPage Fragment"
        const val GALLERY_CODE = 10
    }

    private var selectedImage: Uri? = null

    private lateinit var viewModel: MyPageViewModel
    override fun init() {
        viewModel = MyPageViewModel()
        binding.apply {
        }

        viewModel.loadProfileImage()

        // Result Callback 등록
        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data?.data != null) {
                    selectedImage = it.data?.data
                    Glide.with(requireContext()).load(selectedImage).circleCrop().into(binding.mypageProfileImg)
                } else {
                    Glide.with(requireContext()).load(R.drawable.default_profile_img).circleCrop().into(binding.mypageProfileImg)
                }
            }
        }

        // 프로필 저장 버튼
        binding.mypageProfileSave.setOnClickListener {
            if (selectedImage != null) {
                showProgressBar()
                viewModel.saveProfile(selectedImage!!)
            }
        }

        // 프로필 사진 추가
        binding.mypageProfileImgAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.run {
                getResult.launch(this)
            }
        }

        viewModel.myProfile.observe(this) {
            if (it != "") {
                Glide.with(requireContext()).load(it).circleCrop().into(binding.mypageProfileImg)
            } else {
                Glide.with(requireContext()).load(R.drawable.default_profile_img).circleCrop().into(binding.mypageProfileImg)
            }
        }

        viewModel.isSaved.observe(this) {
            if (it.equals("S")) {
                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            hideProgressBar()
        }
    }

    private fun showProgressBar() {
        blockLayoutTouch()
        binding.mypageProfileLoading.isVisible = true
    }

    private fun hideProgressBar() {
        clearBlockLayoutTouch()
        binding.mypageProfileLoading.isVisible = false
    }

    private fun blockLayoutTouch() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun clearBlockLayoutTouch() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
