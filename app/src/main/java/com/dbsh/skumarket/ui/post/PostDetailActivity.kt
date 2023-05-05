package com.dbsh.skumarket.ui.post

import android.animation.ValueAnimator
import android.content.Intent
import android.util.Log
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.FrameAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityPostDetailBinding
import com.dbsh.skumarket.ui.chat.ChatActivity
import com.dbsh.skumarket.util.Resource
import com.google.android.material.tabs.TabLayoutMediator

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(R.layout.activity_post_detail) {

    private var isHeart = false
    private lateinit var viewModel: PostDetailViewModel
    private lateinit var adapter: FrameAdapter

    override fun init() {
        val postId = intent.getStringExtra("postId")
        val uid = intent.getStringExtra("uid")
        viewModel = PostDetailViewModel()

        if (postId != null && uid != null) {
            viewModel.loadPost(postId, uid)
        }

        binding.postChat.setOnClickListener {
            Intent(this, ChatActivity::class.java).run {
                putExtra("opponent", binding.postNickname.text.toString())
                putExtra("opponentId", uid)
                startActivity(this)
            }
        }

        binding.postLike.setOnClickListener {
            val animator: ValueAnimator = if (isHeart)
                ValueAnimator.ofFloat(0.5f, 1f).setDuration(500)
            else
                ValueAnimator.ofFloat(0f, 0.5f).setDuration(500)
            animator.addUpdateListener {
                binding.postLike.progress = it.animatedValue as Float
            }
            animator.start()
            isHeart = !isHeart
        }

        viewModel.loadProfileLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.postChat.isClickable = false
                    Log.d("loadPostLiveData", "프로필 로드중")
                }
                is Resource.Success -> {
                    Log.d("loadPostLiveData", "프로필 로드 성공")
                    binding.user = it.data
                    if (!it.data?.profileImage.isNullOrBlank()) {
                        Glide.with(this).load(it.data?.profileImage).circleCrop()
                            .into(binding.postProfileImage)
                    } else {
                        Glide.with(this).load(R.drawable.default_profile_img).circleCrop()
                            .into(binding.postProfileImage)
                    }
                    binding.postChat.isClickable = true
                }
                is Resource.Error -> {
                    Log.e("loadPostLiveData", it.message.toString())
                }
            }
        }

        viewModel.loadPostLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("loadPostLiveData", "게시글 로드중")
                }
                is Resource.Success -> {
                    Log.d("loadPostLiveData", "게시글 로드 성공")
                    binding.post = it.data

                    adapter = FrameAdapter(it.data!!.images)
                    binding.postViewPager.adapter = adapter
                    TabLayoutMediator(binding.postTabLayout, binding.postViewPager) { tab, _ ->
                        binding.postViewPager.currentItem = tab.position
                    }.attach()

                }
                is Resource.Error -> {
                    Log.e("loadPostLiveData", it.message.toString())
                }
            }
        }
    }
}