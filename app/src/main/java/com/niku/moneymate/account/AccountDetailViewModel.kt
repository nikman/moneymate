package com.niku.moneymate.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class AccountDetailViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    private val accountIdLiveData = MutableLiveData<UUID>()

    var accountLiveData: LiveData<AccountWithCurrency?> =
        Transformations.switchMap(accountIdLiveData) {
            accountId -> moneyMateRepository.getAccount(accountId)
        }

    fun loadAccount(accountId: UUID) {
        accountIdLiveData.value = accountId
    }

    fun saveAccount(account: AccountWithCurrency) {
        moneyMateRepository.updateAccount(account)
    }

}