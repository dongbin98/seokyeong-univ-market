package com.dbsh.skumarket.ui.login

import android.content.Intent
import android.widget.Toast
import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityMainBinding
import com.dbsh.skumarket.ui.main.BottomActivity
import com.dbsh.skumarket.ui.regist.RegisterActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

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
            Intent(this@MainActivity, RegisterActivity::class.java).run { startActivity(this) }
        }

        // 비밀번호 찾기 이동
        binding.mainFindAccount.setOnClickListener {
            //
        }

        // 로그인 처리
        viewModel.loginState.observe(this) {
            if (it != null) {
                println(it.toString())
                if (it.equals("S")) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    Intent(this@MainActivity, BottomActivity::class.java).run { startActivity(this) }
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loginUser.observe(this) {
            if (it != null) {
                println(it.email)
            }
        }
    }
}
