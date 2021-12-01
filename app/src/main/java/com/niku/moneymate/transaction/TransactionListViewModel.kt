package com.niku.moneymate.transaction

import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository

class TransactionListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val transactionListLiveData = moneyMateRepository.getTransactions()

    fun addTransaction(moneyTransaction: MoneyTransaction) {
        moneyMateRepository.addTransaction(moneyTransaction)
    }

}