package com.niku.moneymate.account

import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class AccountListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()

    val accountListLiveData = moneyMateRepository.getAllAccounts()
    val accountWithCurrencyListLiveData = moneyMateRepository.getAccountsWithBalance()

    fun accountBalanceLiveData(accountId: UUID) = moneyMateRepository.getAccountBalance(accountId)

    fun addAccount(account: Account) {
        moneyMateRepository.addAccount(account)
    }

}