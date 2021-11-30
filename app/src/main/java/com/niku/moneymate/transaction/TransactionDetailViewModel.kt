package com.niku.moneymate.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class TransactionDetailViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    private val transactionIdLiveData = MutableLiveData<UUID>()

    var transactionLiveData: LiveData<TransactionWithProperties?> =
        Transformations.switchMap(transactionIdLiveData) {
                transactionId -> moneyMateRepository.getTransaction(transactionId)
        }

    fun loadTransaction(transactionId: UUID) {
        transactionIdLiveData.value = transactionId
    }

    fun saveTransaction(transaction: MoneyTransaction) {
        moneyMateRepository.updateTransaction(transaction)
    }

}