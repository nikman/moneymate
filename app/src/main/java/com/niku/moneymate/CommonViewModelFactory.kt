package com.niku.moneymate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommonViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}