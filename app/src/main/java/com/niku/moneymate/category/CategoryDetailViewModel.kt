package com.niku.moneymate.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class CategoryDetailViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    private val categoryIdLiveData = MutableLiveData<UUID>()

    var categoryLiveData: LiveData<Category?> =
        Transformations.switchMap(categoryIdLiveData) {
                categoryId -> moneyMateRepository.getCategory(categoryId)
        }

    fun loadCategory(categoryId: UUID) {
        categoryIdLiveData.value = categoryId
    }

    fun saveCategory(category: Category) {
        moneyMateRepository.updateCategory(category)
    }

}