package com.dbsh.skumarket.ui.post

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbsh.skumarket.R
import com.dbsh.skumarket.adapters.SellingAdapter
import com.dbsh.skumarket.util.LinearLayoutManagerWrapper
import com.dbsh.skumarket.databinding.FragmentHomeBinding
import com.dbsh.skumarket.api.model.SellingModelData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var sellingDB: DatabaseReference
    private lateinit var userDB: DatabaseReference
    private lateinit var sellingAdapter: SellingAdapter
    private val sellingList = mutableListOf<SellingModelData>()

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val sellingModelData = snapshot.getValue<SellingModelData>()
            sellingModelData ?: return

            sellingList.add(sellingModelData) // 리스트에 새로운 항목을 더함
            sellingAdapter.submitList(sellingList) // 어댑터 리스트 등록
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private var binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Yang", "onViewCreated")

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        sellingList.clear() // 리스트 초기화

        sellingDB = Firebase.database.reference.child("Selling") // 디비 가져오기;
        userDB = Firebase.database.reference.child("User")

        initSellingAdapter(view)

        initSellingRecyclerView()

        initButton(view)
        // 데이터를 가져옴
        initListener()

    }

    private fun initSellingRecyclerView() {
        // activity 일 때는 this지만
        // 프레그먼트의 경우는 context -> 오류가 있어서 바꿔야 함 클래스 자체를 포괄적으로 기본적을
        val manager = LinearLayoutManager(context)

        manager.reverseLayout = true
        manager.stackFromEnd = true

        binding?:return
        binding!!.sellingRecyclerView.layoutManager =
            LinearLayoutManagerWrapper(context)
        binding!!.sellingRecyclerView.adapter = sellingAdapter
        binding!!.sellingRecyclerView.layoutManager = manager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun initListener() {
        sellingDB.addChildEventListener(listener)
    }

    private fun initButton(view: View) {
        // 내 물건 팔기 버튼;
        binding!!.addButton.setOnClickListener {
            context?.let{
                val intent = Intent(it, UploadPostActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initSellingAdapter(view: View) {
        sellingAdapter = SellingAdapter(onItemClicked = {
            if(auth.currentUser != null){
                // 로그인 상태;
            }else{
                // 로그아웃 상태;
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        sellingDB.removeEventListener(listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        sellingAdapter.notifyDataSetChanged() // view 를 다시 그림
    }
}