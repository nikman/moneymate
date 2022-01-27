package com.niku.moneymate.currency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class CurrencyDetailViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    private val currencyIdLiveData = MutableLiveData<UUID>()

    var currencyLiveData: LiveData<MainCurrency?> =
        Transformations.switchMap(currencyIdLiveData) {
                currencyId -> moneyMateRepository.getCurrency(currencyId)
        }

    fun loadCurrency(currencyId: UUID) {
        currencyIdLiveData.value = currencyId
    }

    fun saveCurrency(currency: MainCurrency) {
        moneyMateRepository.updateCurrency(currency)
    }

    /*fun getDefaultCurrency(): MainCurrency {
        moneyMateRepository.getDefaultCurrency()
    }*/

}