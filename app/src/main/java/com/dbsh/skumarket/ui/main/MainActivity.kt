package com.dbsh.skumarket.ui.main

import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dbsh.skumarket.R
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var viewModel: MainViewModel

    override fun init() {
        viewModel = MainViewModel()
        binding.apply {
            viewModel = viewModel
            executePendingBindings()
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.navigationView).setupWithNavController(navController)
    }
}
