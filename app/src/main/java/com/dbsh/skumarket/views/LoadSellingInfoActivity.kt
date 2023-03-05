package com.dbsh.skumarket.views

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.ActivitySellingInfoBinding
import com.dbsh.skumarket.model.SellingModelData
import com.dbsh.skumarket.viewmodels.LoadViewModel
import com.dbsh.skumarket.viewmodels.RegisterViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_selling_info.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.security.AccessController.getContext

class LoadSellingInfoActivity : AppCompatActivity() {
    private var mbinding: ActivitySellingInfoBinding? = null
    private val binding get() = mbinding!!
    private var isHearting: Boolean = false
    val postKey = "post1"
    val database = Firebase.database
    val postRef = database.getReference("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivitySellingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<SellingModelData>("data")
        binding.titletext.text = data!!.title
        binding.datetext.text = data!!.posttime
        binding.pricetext.text = data!!.price
        binding.infotext.text = data!!.contents
        binding.nickname.text = data!!.uId.toString()

        Glide.with(this).load(data.imageUrl).into(binding.SellingImg)
        Glide.with(this).load(R.drawable.default_profile_img).circleCrop().into(binding.profileImg)
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