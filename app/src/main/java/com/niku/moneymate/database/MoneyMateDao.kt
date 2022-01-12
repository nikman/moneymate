package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project
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
    @Query("SELECT * FROM account WHERE account_id=(:account_id)")
    fun getAccount(account_id: UUID): LiveData<AccountWithCurrency?>

    @Transaction
    @Query("SELECT * FROM project WHERE project_id=(:project_id)")
    fun getProject(project_id: UUID): LiveData<Project?>

    @Query("""
        SELECT acc.initial_balance + ifnull(SUM(mt.amount_from * cat.category_type), 0.0) as balance
        FROM account as acc
        LEFT JOIN moneyTransaction AS mt
        ON acc.account_id = mt.account_id_from
        LEFT JOIN category as cat
        ON mt.category_id = cat.category_id
        WHERE mt.account_id_from=(:id)
        GROUP BY acc.account_id
        """)
    fun getAccountBalance(id: UUID): LiveData<Double?>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT acc.initial_balance + ifnull(SUM(mt.amount_from * cat.category_type), 0.0) AS balance,
            acc.initial_balance AS initial_balance,
            acc.title AS title,
            acc.note AS note,
            acc.account_id AS account_id,
            acc.currency_id AS currency_id,
            cur.currency_code AS currency_code,
            cur.currency_title AS currency_title
        FROM account as acc
        LEFT JOIN moneyTransaction AS mt 
            ON acc.account_id = mt.account_id_from
        LEFT JOIN category as cat
            ON mt.category_id = cat.category_id
        LEFT JOIN mainCurrency AS cur
            ON acc.currency_id = cur.currency_id
        GROUP BY acc.account_id,acc.title,acc.note,
            acc.currency_id,cur.currency_code,
            cur.currency_title
            """)
    fun getAccountsWithBalance(): LiveData<List<AccountWithCurrency>>

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

    @Update
    fun updateProject(project: Project)

    @Insert
    fun addProject(project: Project)

    @Transaction
    @Query("SELECT * FROM project")
    fun getProjects(): LiveData<List<Project>>

    @Transaction
    @Delete
    fun deleteProject(project: Project)

    @Query("SELECT COUNT(transaction_id) FROM moneyTransaction WHERE category_id=(:category_id)")
    fun getTransactionsCountByCategory(category_id: UUID): LiveData<Int?>

    @Query("SELECT COUNT(transaction_id) FROM moneyTransaction WHERE project_id=(:project_id)")
    fun getTransactionsCountByProject(project_id: UUID): LiveData<Int?>

    @Query("SELECT amount_from FROM moneyTransaction WHERE account_id_from=(:account_id)")
    fun getAccountExpensesData(account_id: UUID): LiveData<List<Double?>>

}