package com.dbsh.skumarket.ui.post

import android.content.Intent
import android.view.View
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.PostAdapter
import com.dbsh.skumarket.api.model.ChatList
import com.dbsh.skumarket.api.model.PostList
import com.dbsh.skumarket.base.BaseFragment
import com.dbsh.skumarket.databinding.FragmentPostListBinding
import com.dbsh.skumarket.ui.main.MainActivity
import com.dbsh.skumarket.util.LinearLayoutManagerWrapper

class PostListFragment : BaseFragment<FragmentPostListBinding>(R.layout.fragment_post_list) {

    private lateinit var viewModel: PostListViewModel
    private lateinit var adapter: PostAdapter
    private var postList = mutableListOf<PostList>()

    override fun init() {
        viewModel = PostListViewModel()
        binding.apply {
            viewModel = viewModel
        }
        // RecyclerView Setting
        adapter = PostAdapter(postList as ArrayList<PostList>)
        adapter.apply {
            setOnItemClickListener(object : PostAdapter.OnItemClickListener {
                override fun onItemClick(v: View, data: PostList, position: Int) {

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
            Intent(context, UploadPostActivity::class.java).run { startActivity(this) }
        }

//        viewModel.loadPosts()
    }
}