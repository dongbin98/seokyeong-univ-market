package com.dbsh.skumarket.views

import android.content.Intent
import android.widget.Toast
import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityMainBinding
import com.dbsh.skumarket.viewmodels.LoginViewModel

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var viewModel : LoginViewModel

    override fun init() {
        viewModel = LoginViewModel()
        binding.apply {
            viewModel = viewModel
        }

        binding.mainLoginButton.setOnClickListener {
            if(binding.mainLoginId.toString().isNullOrBlank() || binding.mainLoginPw.toString().isNullOrBlank()) {
                Toast.makeText(this, "아이디 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(binding.mainLoginId.toString(), binding.mainLoginPw.toString())
            }
        }

        // 회원가입 이동
        binding.mainRegistAccount.setOnClickListener{
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 비밀번호 찾기 이동
        binding.mainFindAccount.setOnClickListener{
            //
        }

        viewModel.loginState.observe(this) {
            println(it.toString())
            if(it != null) {
                if(it.equals("S")) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}