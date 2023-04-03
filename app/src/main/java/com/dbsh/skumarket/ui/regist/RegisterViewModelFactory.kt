package com.dbsh.skumarket.ui.regist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dbsh.skumarket.api.SkuAuthApi

class RegisterViewModelFactory(private val api: SkuAuthApi): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return RegisterViewModel(api) as T
    }
}