package com.dbsh.skumarket.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivityHomeBinding
import com.dbsh.skumarket.databinding.ActivityMainBinding
import com.dbsh.skumarket.viewmodels.HomeViewModel
import com.dbsh.skumarket.viewmodels.LoginViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = HomeViewModel()
        binding.apply {
            lifecycleOwner = this@HomeActivity
            viewModel = viewModel
            executePendingBindings()
        }
        binding.bottomNavi.menu.findItem(R.id.menu_center).isChecked = true
        replaceFragment(PostFragment())

        binding.bottomNavi.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_left -> {
                    replaceFragment(ChatFragment())
                    true
                }
                R.id.menu_center -> {
                    replaceFragment(PostFragment())
                    true
                }
                R.id.menu_right -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.mainContainer.id, fragment)
        fragmentTransaction.commit()
    }
}