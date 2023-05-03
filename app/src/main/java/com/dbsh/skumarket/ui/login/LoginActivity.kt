package com.dbsh.skumarket.ui.login

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityLoginBinding
import com.dbsh.skumarket.ui.main.MainActivity
import com.dbsh.skumarket.ui.register.RegisterActivity
import com.dbsh.skumarket.util.Resource

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var viewModel: LoginViewModel

    override fun init() {
        viewModel = LoginViewModel()
        binding.apply {
            viewModel = viewModel
        }

        binding.mainLoginButton.setOnClickListener {
            if (binding.mainLoginId.text.toString().isBlank() || binding.mainLoginPw.text.toString().isBlank()) {
                Toast.makeText(this, "아이디 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(binding.mainLoginId.text.toString(), binding.mainLoginPw.text.toString())
            }
        }

        // 회원가입 이동
        binding.mainRegistAccount.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).run { startActivity(this) }
        }

        // 비밀번호 찾기 이동
        binding.mainFindAccount.setOnClickListener {
            //
        }

        // 로그인 처리
        viewModel.loginState.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showProgress()
                }
                is Resource.Success -> {
                    hideProgress()
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    Intent(this@LoginActivity, MainActivity::class.java).run { startActivity(this) }
                }
                is Resource.Error -> {
                    hideProgress()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showProgress() {
        binding.loginProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.loginProgressBar.visibility = View.GONE
    }
}
