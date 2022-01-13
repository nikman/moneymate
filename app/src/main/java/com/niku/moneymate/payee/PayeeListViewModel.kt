package com.niku.moneymate.payee

import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository

class PayeeListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val payeeListLiveData = moneyMateRepository.getPayees()

    fun addPayee(payee: Payee) {
        moneyMateRepository.addPayee(payee)
    }

}