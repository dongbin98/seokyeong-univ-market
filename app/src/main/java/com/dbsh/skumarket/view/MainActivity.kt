package com.dbsh.skumarket.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivityMainBinding
import com.dbsh.skumarket.retrofit.RequestLoginData
import com.dbsh.skumarket.retrofit.ResponseLogin
import com.dbsh.skumarket.retrofit.RetrofitClient
import com.dbsh.skumarket.service.LoginService
import com.dbsh.skumarket.viewmodels.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = LoginViewModel()
        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.btLogin.setOnClickListener {
            viewModel.getUserData(binding.etLoginId.text.toString(), binding.etLoginPw.text.toString())
        }

        viewModel.loginState.observe(this) {
            Log.d("SKUM", "Network State : $it")
        }

        viewModel.loginData.observe(this) {
            if(it != null) {
                Log.d("SKUM", "Name : ${it.userInfo?.korName}")
                val intent = Intent(this, bottomActivity::class.java)
                startActivity(intent)
            }
        }
    }
}