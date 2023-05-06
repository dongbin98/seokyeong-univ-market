package com.dbsh.skumarket.ui.post

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.PostAdapter
import com.dbsh.skumarket.api.model.PostList
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.databinding.FragmentPostListBinding
import com.dbsh.skumarket.ui.main.MainActivity
import com.dbsh.skumarket.util.LinearLayoutManagerWrapper
import com.dbsh.skumarket.util.Resource

class PostListFragment : BaseFragment<FragmentPostListBinding>(R.layout.fragment_post_list) {

    private lateinit var viewModel: PostListViewModel
    private lateinit var adapter: PostAdapter
    private var postList = mutableListOf<PostList>()

//    override fun onResume() {
//        // 게시글 등록, 갱신, 삭제 후 리스트로 돌아올 때 자동 목록 갱신 -> 모든 Resume 이벤트시 발생함
//        super.onResume()
//        viewModel.loadPosts()
//    }

    // registerForActivityResult 통해 게시글 등록, 갱신, 삭제 이후 프래그먼트로 돌아올 때 List Update
    private val postLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == 100) {
                Log.d("postLauncher", "Post List is updated!!")
                viewModel.loadPosts()
            }
        }


    override fun init() {
        viewModel = PostListViewModel()
        binding.apply {
            viewModel = viewModel
        }
        // RecyclerView Settings
        adapter = PostAdapter(postList as ArrayList<PostList>)
        adapter.apply {
            setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                override fun onItemClick(v: View, data: PostList, position: Int) {
                    Intent(context, PostDetailActivity::class.java).run {
                        putExtra("postId", data.postId)
                        putExtra("uid", data.uid)
                        postLauncher.launch(this)
                    }
                }
            })
        }

        binding.postListRecyclerview.apply {
            itemAnimator = null
            adapter = this@PostListFragment.adapter
            layoutManager =
                LinearLayoutManagerWrapper(context)
        }

        binding.postListAdd.setOnClickListener {
            Intent(context, UploadPostActivity::class.java).run { postLauncher.launch(this) }
        }

        viewModel.loadPosts()

        viewModel.loadLiveData.observe(this) { result ->
            when(result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    postList.clear()
                    result.data?.let { postList.addAll(it) }
                    adapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}