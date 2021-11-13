package com.niku.moneymate

import androidx.lifecycle.ViewModel

class AccountListViewModel: ViewModel() {

    val accounts = mutableListOf<Account>()

    init {
        for (i in 0 until 5) {
            val account = Account()
            account.title = "Account #$i"
            account.balance = i * 1000 + i;
            accounts += account
        }
    }

}