package com.niku.moneymate.payee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class PayeeDetailViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    private val payeeIdLiveData = MutableLiveData<UUID>()

    /*var accountLiveData: LiveData<AccountWithCurrency?> =
        Transformations.switchMap(accountIdLiveData) {
            accountId -> moneyMateRepository.getAccount(accountId)
        }

    fun loadAccount(accountId: UUID) {
        accountIdLiveData.value = accountId
    }

    fun saveAccount(account: Account) {
        moneyMateRepository.updateAccount(account)
    }

    fun getAccountBalance(id: UUID): LiveData<Double?> {
        return moneyMateRepository.getAccountBalance(id)
    }

    fun getAccountExpensesData(account_id: UUID): LiveData<List<Double?>> {
        return moneyMateRepository.getAccountExpensesData(account_id)
    }*/

}