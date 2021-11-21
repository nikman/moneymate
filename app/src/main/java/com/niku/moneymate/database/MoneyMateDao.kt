package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.niku.moneymate.account.Account
import com.niku.moneymate.currency.MainCurrency
import java.util.*

@Dao
interface MoneyMateDao {

    @Query("SELECT * FROM account")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE id=(:id)")
    fun getAccount(id: UUID): LiveData<Account?>

    @Update
    fun updateAccount(account: Account)

    @Insert
    fun addAccount(account: Account)

    @Query("SELECT * FROM mainCurrency")
    fun getCurrencies(): LiveData<List<MainCurrency>>

    @Query("SELECT * FROM mainCurrency WHERE id=(:id)")
    fun getCurrency(id: UUID): LiveData<MainCurrency>

    @Update
    fun updateCurrency(currency: MainCurrency)

    @Insert
    fun insertCurrency(currency: MainCurrency)

}