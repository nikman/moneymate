package com.niku.moneymate.transaction

import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class TransactionListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val transactionListLiveData = moneyMateRepository.getTransactions()

    fun addTransaction(moneyTransaction: MoneyTransaction) {
        moneyMateRepository.addTransaction(moneyTransaction)
    }

    fun getTransactionsCountByCategory(category_id: UUID) {
        moneyMateRepository.getTransactionsCountByCategory(category_id)
    }

    fun getTransactionsCountByProject(project_id: UUID) {
        moneyMateRepository.getTransactionsCountByProject(project_id)
    }

}