package com.dbsh.skumarket.ui.post

import android.animation.ValueAnimator
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.FrameAdapter
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityPostDetailBinding
import com.dbsh.skumarket.ui.chat.ChatActivity
import com.dbsh.skumarket.util.Resource
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(R.layout.activity_post_detail) {

    private var isHeart = false
    private lateinit var viewModel: PostDetailViewModel
    private lateinit var adapter: FrameAdapter
    private val auth by lazy { Firebase.auth }
    private val myUid = auth.currentUser?.uid

    private val postLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == 100) {
                // 게시글 수정(UploadPostActivity) 후 해당 Activity 에 돌아왔을 때 해당 뷰를 갱신하기 위함
                Log.d("postLauncher", "Post List is updated")
                val postId = it.data!!.getStringExtra("postId")
                if(postId != null && myUid != null) // 결국 작성자 본인의 uid임으로 myUid로 로드해옴
                    viewModel.loadPost(postId, myUid)
            }
        }

    override fun init() {
        // PostListFragment 에서 게시물을 눌러 해당 Activity 에 들어왔을 경우
        viewModel = PostDetailViewModel()

        val postId = intent.getStringExtra("postId")
        val uid = intent.getStringExtra("uid")

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

        binding.postDelete.setOnClickListener {
            viewModel.deletePost(postId.toString())
        }

        binding.postUpdate.setOnClickListener {
            // update 보류 (storage 문제)
//            Intent(this, UploadPostActivity::class.java).run {
//                putExtra("uid", uid)
//                putExtra("postId", postId)
//                postLauncher.launch(this)
//            }
        }

        viewModel.loadProfileLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.postChat.isVisible = false
                    binding.postDelete.isVisible = false
                    binding.postUpdate.isVisible = false
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
                    if(uid.equals(myUid)) { // 내 글이라면 수정 삭제 허용 및 채팅하기 없앰
                        binding.postDelete.isVisible = true
                        binding.postUpdate.isVisible = true
                        binding.postChat.isVisible = false
                    } else {
                        binding.postChat.isVisible = true
                    }
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

        viewModel.deletePostLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("deletePostLiveData", "게시글 삭제중")
                }
                is Resource.Success -> {
                    Log.d("deletePostLiveData", "게시글 삭제 성공")
                    setResult(100, intent)
                    finish()
                }
                is Resource.Error -> {
                    Log.e("deletePostLiveData", it.message.toString())
                }
            }
        }
    }
}