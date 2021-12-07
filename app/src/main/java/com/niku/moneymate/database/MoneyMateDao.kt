package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionWithProperties
import java.util.*

@Dao
interface MoneyMateDao {

    @Transaction
    @Query("SELECT * FROM account")
    fun getAllAccounts(): LiveData<List<Account>>

    @Transaction
    @Query("SELECT * FROM account")
    fun getAccounts(): LiveData<List<AccountWithCurrency>>

    @Transaction
    @Query("SELECT * FROM account WHERE account_id=(:id)")
    fun getAccount(id: UUID): LiveData<AccountWithCurrency?>

    @Query(
        "SELECT acc.balance + SUM(mt.amount * cat.category_type)" +
                " FROM moneyTransaction AS mt" +
                " LEFT JOIN category as cat" +
                " ON mt.category_id = cat.category_id" +
                " LEFT JOIN account as acc" +
                " ON mt.account_id = acc.account_id" +
                " WHERE mt.account_id=(:id)"
    )
    fun getAccountBalance(id: UUID): LiveData<Double?>

    @Query(
        "SELECT acc.balance + SUM(mt.amount * cat.category_type) AS balance," +
                "   acc.title AS title," +
                "   acc.note AS note," +
                "   acc.account_id AS account_id," +
                "   acc.currency_id AS currency_id," +
                "   cur.currency_code AS currency_code," +
                "   cur.currency_title AS currency_title" +
                "   FROM account as acc" +
                "   LEFT JOIN moneyTransaction AS mt " +
                "       LEFT JOIN category as cat" +
                "       ON mt.category_id = cat.category_id" +
                "   ON acc.account_id = mt.account_id" +
                "   LEFT JOIN mainCurrency AS cur" +
                "   ON acc.currency_id = cur.currency_id" +
                "   GROUP BY acc.account_id,acc.title,acc.note," +
                "   acc.currency_id,cur.currency_code," +
                "   cur.currency_title"
    )
    fun getAccountsBalance(): LiveData<List<Double>>

    @Update
    fun updateAccount(account: Account)

    @Insert
    fun addAccount(account: Account)

    @Query("SELECT * FROM mainCurrency")
    fun getCurrencies(): LiveData<List<MainCurrency>>

    @Query("SELECT * FROM mainCurrency WHERE currency_id=(:id)")
    fun getCurrency(id: UUID): LiveData<MainCurrency?>

    /*@Query("SELECT * FROM mainCurrency WHERE currency_is_default limit 1")
    fun getDefaultCurrency(): LiveData<MainCurrency>*/

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

    @Transaction
    @Query("SELECT * FROM moneyTransaction")
    fun getTransactions(): LiveData<List<TransactionWithProperties>>

    @Transaction
    @Query("SELECT * FROM moneyTransaction WHERE transaction_id=(:id)")
    fun getTransaction(id: UUID): LiveData<TransactionWithProperties?>

    @Update
    fun updateTransaction(transaction: MoneyTransaction)

    @Insert
    fun addTransaction(transaction: MoneyTransaction)

}