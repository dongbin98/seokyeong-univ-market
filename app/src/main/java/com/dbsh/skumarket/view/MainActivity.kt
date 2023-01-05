package com.dbsh.skumarket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        viewModel.loginData.observe(this) {
            if(it != null) {
                Toast.makeText(this, it.getRtnStatus().toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}