package com.dbsh.skumarket.ui.register

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dbsh.skumarket.R
import com.dbsh.skumarket.api.provideSkuAuthApi
import com.dbsh.skumarket.base.BaseActivity
import com.dbsh.skumarket.databinding.ActivityRegisterBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    private val viewModelFactory by lazy { RegisterViewModelFactory(provideSkuAuthApi()) }
    lateinit var viewModel: RegisterViewModel
    lateinit var uid: String
    lateinit var name: String
    var isAuth: Boolean = false // 학생인증 여부
    private val disposable = CompositeDisposable()

    override fun init() {
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
        binding.apply {
            viewModel = viewModel
        }

        // 학생인증 버튼 클릭 이벤트처리
        binding.registerSkuAuth.setOnClickListener {
            viewModel.getSkuAuth(
                binding.registerSkuId.text.toString(),
                binding.registerSkuPw.text.toString()
            )
        }

        // 회원가입 버튼 클릭 이벤트처리
        binding.registerButton.setOnClickListener {
            if (isAuth) {
                viewModel.signUp(
                    binding.registerId.text.toString(),
                    binding.registerPw.text.toString(),
                    name,
                    uid
                )
            } else {
                Toast.makeText(this, "서경대학교 학생인증을 해야합니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 학생 인증 오류
        disposable.add(viewModel.message
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                showError(message)
            })

        // 학생 인증 로딩
        disposable.add(viewModel.isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isLoading ->
                if (isLoading) showProgress()
                else hideProgress()
            })

        // 학생인증 통신 체크
        disposable.add(viewModel.rtnStatus
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { if (!it.equals("S"))
                Toast.makeText(this, "인증 실패, 에러 코드 : $it", Toast.LENGTH_SHORT).show()
                Log.d("SKUM", "Network State : $it")
            })

        // 학생인증 처리
        disposable.add(viewModel.result
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it != null) {
                    uid = it.id.toString()
                    name = it.korName.toString()
                    Toast.makeText(this, "${it.korName}님 인증되었습니다", Toast.LENGTH_SHORT).show()
                    binding.registerSkuAuth.setBackgroundResource(R.drawable.button_gray_radius_20)
                    isAuth = true
                    binding.registerSkuAuth.isClickable = false
                    binding.registerSkuAuth.text = "인증완료"
                }
            })

        // 회원가입 처리
        viewModel.registerState.observe(this) {
            if (it != null) {
                if (it.equals("S")) {
                    Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "회원가입이 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun showProgress() {
        binding.registerProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.registerProgressBar.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
