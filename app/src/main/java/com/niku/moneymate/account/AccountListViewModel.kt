package com.niku.moneymate.account

import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository

class AccountListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val accountListLiveData = moneyMateRepository.getAllAccounts()
    //val accountWithCurrencyListLiveData = moneyMateRepository.getAccounts()
    val accountWithCurrencyListLiveData = moneyMateRepository.getAccountsWithBalance()

    fun addAccount(account: Account) {
        moneyMateRepository.addAccount(account)
    }

}