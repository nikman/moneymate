package com.niku.moneymate.category

import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository

class CategoryListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val categoryListLiveData = moneyMateRepository.getCategories()

    fun addCategory(category: Category) {
        moneyMateRepository.addCategory(category)
    }

}