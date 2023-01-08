package com.dbsh.skumarket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.databinding.DataBindingUtil
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivityBottomBinding


private const val TAG_HOME = "home_fragment"
private const val TAG_CHATTING = "chatting_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"

class bottomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.calenderFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.homeFragment -> setFragment(TAG_CHATTING, HomeFragment())
                R.id.myPageFragment-> setFragment(TAG_MY_PAGE, MyPageFragment())
            }
            true
        }
    }
    private fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrame, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val chatting = manager.findFragmentByTag(TAG_CHATTING)
        val myPage = manager.findFragmentByTag(TAG_MY_PAGE)

        if (home != null){
            fragTransaction.hide(home)
        }

        if (chatting != null){
            fragTransaction.hide(chatting)
        }

        if (myPage != null) {
            fragTransaction.hide(myPage)
        }

        if (tag == TAG_HOME) {
            if (home!=null){
                fragTransaction.show(home)
            }
        }
        else if (tag == TAG_CHATTING) {
            if (chatting != null) {
                fragTransaction.show(chatting)
            }
        }

        else if (tag == TAG_MY_PAGE){
            if (myPage != null){
                fragTransaction.show(myPage)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}


