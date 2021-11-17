package com.niku.moneymate

import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository

class AccountListViewModel: ViewModel() {

    //val accounts = mutableListOf<Account>()
    private val moneyMateRepository = MoneyMateRepository.get()
    val accountListLiveData = moneyMateRepository.getAccounts()

    fun addAccount(account: Account) {
        moneyMateRepository.addAccount(account)
    }

}