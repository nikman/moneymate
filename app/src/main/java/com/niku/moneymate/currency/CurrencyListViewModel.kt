package com.niku.moneymate.currency

import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository

class CurrencyListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val currencyListLiveData = moneyMateRepository.getCurrencies()

    fun addCurrency(currency: MainCurrency) {
        moneyMateRepository.addCurrency(currency)
    }

    fun deleteCurrency(currency: MainCurrency) {
        moneyMateRepository.deleteCurrency(currency)
    }

}