package com.dbsh.skumarket.views

import android.util.Log
import android.widget.Toast
import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityRegisterBinding
import com.dbsh.skumarket.viewmodels.RegisterViewModel

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    private lateinit var viewModel : RegisterViewModel
    lateinit var uid: String
    lateinit var name: String
    var isAuth: Boolean = false // 학생인증 여부

    override fun init() {
        viewModel = RegisterViewModel()
        binding.apply {
            viewModel = viewModel
        }

        // 학생인증 버튼 클릭 이벤트처리
        binding.registerSkuAuth.setOnClickListener{
            viewModel.getSkuAuth(binding.registerSkuId.text.toString(), binding.registerSkuPw.text.toString())
        }

        // 회원가입 버튼 클릭 이벤트처리
        binding.registerButton.setOnClickListener {
            if(isAuth)
                viewModel.signUp(binding.registerId.text.toString(), binding.registerPw.text.toString(), name, uid)
            else {
                Toast.makeText(this, "서경대학교 학생인증을 해야합니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 학생인증 통신 체크
        viewModel.authState.observe(this) {
            Log.d("SKUM", "Network State : $it")
        }

        // 학생인증 처리
        viewModel.authData.observe(this) {
            if(it != null) {
                uid = it.userInfo?.id.toString()
                name = it.userInfo?.korName.toString()
                Toast.makeText(this, "${it.userInfo?.korName}님 인증되었습니다", Toast.LENGTH_SHORT).show()
                binding.registerSkuAuth.setBackgroundResource(R.drawable.button_gray_radius_20)
                isAuth = true
                binding.registerSkuAuth.isClickable = false
            }
        }

        // 회원가입 처리
        viewModel.registerState.observe(this) {
            if(it != null) {
                if(it.equals("S")) {
                    Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else
                    Toast.makeText(this, "회원가입이 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}