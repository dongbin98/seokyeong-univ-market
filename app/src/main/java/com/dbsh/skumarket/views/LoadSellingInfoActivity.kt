package com.dbsh.skumarket.views

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivitySellingInfoBinding
import com.dbsh.skumarket.model.SellingModelData
import com.dbsh.skumarket.viewmodels.LoadViewModel
import com.dbsh.skumarket.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.activity_selling_info.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class LoadSellingInfoActivity : AppCompatActivity() {
    private var mbinding: ActivitySellingInfoBinding? = null
    private val binding get() = mbinding!!
    private var isHearting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivitySellingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<SellingModelData>("data")
        binding.titletext.text = data!!.title
        binding.datetext.text = data!!.posttime
        binding.pricetext.text = data!!.price
        binding.infotext.text = data!!.contents
        binding.nickname.text = data!!.uId
        Glide.with(this).load(data.imageUrl).into(SellingImg)
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