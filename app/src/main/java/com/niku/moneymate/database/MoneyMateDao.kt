package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountExpenses
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
    @Query("SELECT * FROM account WHERE is_active ORDER BY sort_order DESC")
    fun getAllAccounts(): LiveData<List<Account>>

    @Transaction
    @Query("SELECT * FROM account WHERE is_active ORDER BY sort_order DESC")
    fun getAccounts(): LiveData<List<AccountWithCurrency>>

    @Transaction
    @Query("SELECT * FROM account WHERE account_id=(:account_id)")
    fun getAccount(account_id: UUID): LiveData<AccountWithCurrency?>

    @Query("SELECT * FROM account WHERE account_id=(:account_id)")
    fun getAccountDirect(account_id: UUID): Account?

    @Query("SELECT * FROM account LIMIT 1")
    fun getAccountDirect(): Account?

    @Query("SELECT * FROM maincurrency LIMIT 1")
    fun getCurrencyDirect(): MainCurrency?

    @Transaction
    @Query("SELECT * FROM project WHERE project_id=(:project_id)")
    fun getProject(project_id: UUID): LiveData<Project?>

    @Query("""
        SELECT SUM(Inn.balance) AS balance
        FROM (
            SELECT
                acc.account_id,
                SUM(ifnull(mt.amount_from, 0.0)) as balance
            FROM account as acc
            LEFT JOIN moneyTransaction AS mt
                ON acc.account_id = mt.account_id_from
            WHERE acc.account_id = (:id) AND mt.account_id_from=(:id)
            GROUP BY acc.account_id
        UNION
            SELECT
                acc.account_id,
                SUM(ifnull(mt.amount_to, 0.0)) as balance
            FROM account as acc
            LEFT JOIN moneyTransaction AS mt
               ON acc.account_id = mt.account_id_to
            WHERE acc.account_id = (:id) AND mt.account_id_to=(:id)
            GROUP BY acc.account_id) AS Inn
        GROUP BY Inn.account_id
        """)
    fun getAccountBalance(id: UUID): LiveData<Double?>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT 0.0 AS balance,
            acc.initial_balance AS initial_balance,
            acc.title AS title,
            acc.note AS note,
            acc.account_id AS account_id,
            acc.currency_id AS currency_id,
            acc.is_active AS is_active,
            acc.is_include_into_totals AS is_include_into_totals,
            acc.sort_order AS sort_order,
            acc.account_external_id AS account_external_id
        FROM account as acc
        LEFT JOIN mainCurrency AS cur
            ON acc.currency_id = cur.currency_id
        WHERE acc.is_active = (:showOnlyActive)
        ORDER BY sort_order DESC
            """)
    fun getAccountsWithBalance(showOnlyActive: Boolean = true): LiveData<List<AccountWithCurrency>>

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

    @Delete
    fun deleteCurrency(currency: MainCurrency)

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

    @Delete
    fun deleteCategory(category: Category)

    @Transaction
    @Query("SELECT * FROM moneyTransaction ORDER BY transaction_date DESC")
    fun getTransactions(): LiveData<List<TransactionWithProperties>>

    @Transaction
    @Query("SELECT * FROM moneyTransaction WHERE transaction_id=(:id)")
    fun getTransaction(id: UUID): LiveData<TransactionWithProperties?>

    @Update
    fun updateTransaction(transaction: MoneyTransaction)

    @Insert
    fun addTransaction(transaction: MoneyTransaction)

    @Delete
    fun deleteTransaction(transaction: MoneyTransaction)

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

    @Query("""
        SELECT type,date,SUM(amount) as amount
        FROM (
            SELECT
            transaction_date as transaction_date,
                case  
                    WHEN amount_to < 0 THEN -1
                    WHEN amount_to > 0 THEN 1
                end as type,
           strftime("%m.%Y", datetime(transaction_date / 1000, 'unixepoch', 'start of month')) as date,
           (amount_to) as amount
        FROM moneyTransaction
        WHERE account_id_to = (:account_id) and amount_to <> 0 AND datetime(transaction_date / 1000, 'unixepoch', 'start of month') >= DATE('now', '-6 month')
        
       UNION ALL
       
       SELECT
       transaction_date as transaction_date,
                  case  
                  WHEN amount_from < 0 THEN -1
                  WHEN amount_from > 0 THEN 1
                  end as type,
                  strftime("%m.%Y", datetime(transaction_date / 1000, 'unixepoch', 'start of month')) as date,
                  (amount_from) as amount
               FROM moneyTransaction
               WHERE account_id_from = (:account_id) and account_id_to = "00000004-0001-0001-0001-000000000002" and amount_from <> 0 AND datetime(transaction_date / 1000, 'unixepoch', 'start of month') >= DATE('now', '-6 month')
               ) AS Inn
               GROUP BY Inn.type, Inn.date
               ORDER BY Inn.transaction_date
        """)
    fun getAccountExpensesData(account_id: UUID): LiveData<List<AccountExpenses>>

    @Query("SELECT account_id FROM account WHERE account_external_id=(:externalId)")
    fun getAccountByExternalId(externalId: Int): UUID?

    @Query("SELECT category_id FROM category WHERE category_external_id=(:externalId)")
    fun getCategoryByExternalId(externalId: Int): UUID?

    @Query("SELECT payee_id FROM payee WHERE payee_external_id=(:externalId)")
    fun getPayeeByExternalId(externalId: Int): UUID?

    @Query("SELECT project_id FROM project WHERE project_external_id=(:externalId)")
    fun getProjectByExternalId(externalId: Int): UUID?

    @Query("SELECT * FROM category WHERE category_id=(:categoryUUID)")
    fun getCategoryDirect(categoryUUID: UUID): Category?
}