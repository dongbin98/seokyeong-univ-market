package com.dbsh.skumarket.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivityBottomBinding
import com.dbsh.skumarket.viewmodels.BottomViewModel

private const val TAG_HOME = "home_fragment"
private const val TAG_CHAT = "chat_list_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"

class BottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomBinding
    private lateinit var viewModel: BottomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom)
        viewModel = BottomViewModel()
        binding.apply {
            lifecycleOwner = this@BottomActivity
            viewModel = viewModel
            executePendingBindings()

            navigationView.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.calenderFragment -> setFragment(TAG_HOME, HomeFragment())
                    R.id.homeFragment -> setFragment(TAG_CHAT, ChatListFragment())
                    R.id.myPageFragment-> setFragment(TAG_MY_PAGE, MyPageFragment())
                }
                true
            }
        }
        setFragment(TAG_HOME, HomeFragment())
    }

    // 다른 프레그먼트 화면으로 이동하는 기능
    private fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrame, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val chatting = manager.findFragmentByTag(TAG_CHAT)
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
            if (home != null){
                fragTransaction.show(home)
            }
        }
        else if (tag == TAG_CHAT) {
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


