package com.niku.moneymate

import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository

class AccountListViewModel: ViewModel() {

    //val accounts = mutableListOf<Account>()
    private val moneyMateRepository = MoneyMateRepository.get()
    val accountListLiveData = moneyMateRepository.getAccounts()

    /*init {
        for (i in 0 until 5) {
            val account = Account()
            account.title = "Account #$i"
            account.balance = i * 1000 + i;
            accounts += account
        }
    }*/

}