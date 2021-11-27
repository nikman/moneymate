package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import java.util.*

@Dao
interface MoneyMateDao {

    //@Query("SELECT account.id, account.title, account.balance, account.note, mainCurrency.title FROM account, mainCurrency WHERE account.currency_id = mainCurrency.id")
    @Transaction
    @Query("SELECT * FROM account")
    fun getAccounts(): LiveData<List<AccountWithCurrency>>

    @Transaction
    @Query("SELECT * FROM account WHERE id=(:id)")
    fun getAccount(id: UUID): LiveData<AccountWithCurrency?>

    @Update
    fun updateAccount(account: Account)

    @Insert
    fun addAccount(account: Account)

    @Query("SELECT * FROM mainCurrency")
    fun getCurrencies(): LiveData<List<MainCurrency>>

    @Query("SELECT * FROM mainCurrency WHERE currency_id=(:id)")
    fun getCurrency(id: UUID): LiveData<MainCurrency?>

    @Update
    fun updateCurrency(currency: MainCurrency)

    @Insert
    fun addCurrency(currency: MainCurrency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertAllCurrencies(currencies: List<MainCurrency>)
    fun insertAllCurrencies(currencies: List<MainCurrency>)

    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE category_id=(:id)")
    fun getCategory(id: UUID): LiveData<Category?>

    @Update
    fun updateCategory(category: Category)

    @Insert
    fun addCategory(category: Category)

}