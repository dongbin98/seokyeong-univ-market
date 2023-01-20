package com.dbsh.skumarket.views

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivityMainBinding
import com.dbsh.skumarket.databinding.ActivitySellingInfoBinding
import com.dbsh.skumarket.viewmodels.LoadViewModel
import com.dbsh.skumarket.viewmodels.RegisterViewModel

class LoadSellingInfoActivity : AppCompatActivity() {
    private var mbinding: ActivitySellingInfoBinding? = null
    private val binding get() = mbinding!!
    private var isHearting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivitySellingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickButton(view: View){
        if(!isHearting){
            // 기본이 false이므로 false가 아닐때 실행
            val animator = ValueAnimator.ofFloat(0f, 0.5f).setDuration(500)
            animator.addUpdateListener {
                binding.likeBtn.progress = it.animatedValue as Float
            }
            animator.start()
            isHearting = true // 그리고 트루로 바꿈
        }else{ // 트루일때 실행
            val animator = ValueAnimator.ofFloat(0.5f, 1f).setDuration(500)
            animator.addUpdateListener {
                binding.likeBtn.progress = it.animatedValue as Float
            }
            animator.start()
            isHearting = false // 다시 fasle로 바뀜
        }
    }
}