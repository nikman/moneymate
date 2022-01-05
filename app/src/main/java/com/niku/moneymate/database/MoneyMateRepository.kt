package com.niku.moneymate.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionWithProperties
import java.util.*
import java.util.concurrent.Executors
import androidx.room.RoomDatabase
import com.niku.moneymate.R
import com.niku.moneymate.utils.*


const val DATABASE_NAME = "money-mate-database"
const val TAG = "MoneyMateRepository"

class MoneyMateRepository private constructor(context: Context) {

    private val context = context
    private val executor = Executors.newSingleThreadExecutor()

    var rdc: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            Log.d(TAG, "on create db")
            Executors.newSingleThreadScheduledExecutor().execute {
                val currencyRub =
                    MainCurrency(
                        UUID.fromString(UUID_CURRENCY_RUB),
                        CODE_CURRENCY_RUB,
                        TITLE_CURRENCY_RUB)
                moneyMateDao.addCurrency(currencyRub)
                val currencyUsd =
                    MainCurrency(
                        UUID.fromString(UUID_CURRENCY_USD),
                        CODE_CURRENCY_USD,
                        TITLE_CURRENCY_USD)
                moneyMateDao.addCurrency(currencyUsd)
                val currencyEur =
                    MainCurrency(
                        UUID.fromString(UUID_CURRENCY_EUR),
                        CODE_CURRENCY_EUR,
                        TITLE_CURRENCY_EUR)
                moneyMateDao.addCurrency(currencyEur)

                val projectEmpty =
                    Project(
                        context.resources.getString(R.string.empty_project_title),
                        UUID.fromString(UUID_PROJECT_EMPTY))
                moneyMateDao.addProject(projectEmpty)
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            Log.d(TAG, "on open db")
        }
    }

    private val database : MoneyMateDatabase = Room.databaseBuilder(
            context.applicationContext,
            MoneyMateDatabase::class.java,
            DATABASE_NAME)
        .fallbackToDestructiveMigration()
        //.addMigrations(migrationFrom11To12)
        //.createFromAsset("database/$DATABASE_NAME.db")
        .addCallback(rdc)
        .build() // !

    private val moneyMateDao = database.moneyMateDao()

    fun getAllAccounts(): LiveData<List<Account>> = moneyMateDao.getAllAccounts()
    fun getAccounts(): LiveData<List<AccountWithCurrency>> = moneyMateDao.getAccounts()
    fun getAccountsWithBalance(): LiveData<List<AccountWithCurrency>> = moneyMateDao.getAccountsWithBalance()
    fun getCurrencies(): LiveData<List<MainCurrency>> = moneyMateDao.getCurrencies()
    fun getCategories(): LiveData<List<Category>> = moneyMateDao.getCategories()
    fun getTransactions(): LiveData<List<TransactionWithProperties>> = moneyMateDao.getTransactions()
    fun getProjects(): LiveData<List<Project>> = moneyMateDao.getProjects()

    fun getAccount(id: UUID): LiveData<AccountWithCurrency?> = moneyMateDao.getAccount(id)
    fun getCurrency(id: UUID): LiveData<MainCurrency?> = moneyMateDao.getCurrency(id)
    //fun getDefaultCurrency(): LiveData<MainCurrency?> = moneyMateDao.getDefaultCurrency()
    fun getCategory(id: UUID): LiveData<Category?> = moneyMateDao.getCategory(id)
    fun getTransaction(id: UUID): LiveData<TransactionWithProperties?> = moneyMateDao.getTransaction(id)
    fun getAccountBalance(id: UUID): LiveData<Double?> = moneyMateDao.getAccountBalance(id)
    fun getProject(id: UUID): LiveData<Project?> = moneyMateDao.getProject(id)

    fun updateAccount(account: Account) {
        executor.execute {
            moneyMateDao.updateAccount(account)
        }
    }

    fun updateCurrency(currency: MainCurrency) {
        executor.execute {
            moneyMateDao.updateCurrency(currency)
        }
    }

    fun updateCategory(category: Category) {
        executor.execute {
            moneyMateDao.updateCategory(category)
        }
    }

    fun updateTransaction(transaction: MoneyTransaction) {
        executor.execute {
            moneyMateDao.updateTransaction(transaction)
        }
    }

    fun updateProject(project: Project) {
        executor.execute {
            moneyMateDao.updateProject(project)
        }
    }

    fun addAccount(account: Account) {
        executor.execute {
            moneyMateDao.addAccount(account)
        }
    }

    fun addCurrency(currency: MainCurrency) {
        executor.execute {
            moneyMateDao.addCurrency(currency)
        }
    }

    fun insertAllCurrencies(currencies: List<MainCurrency>) {
        executor.execute {
            moneyMateDao.insertAllCurrencies(currencies)
        }
    }

    fun addCategory(category: Category) {
        executor.execute {
            moneyMateDao.addCategory(category)
        }
    }

    fun addTransaction(transaction: MoneyTransaction) {
        executor.execute {
            moneyMateDao.addTransaction(transaction)
        }
    }

    fun addProject(project: Project) {
        executor.execute {
            moneyMateDao.addProject(project)
        }
    }

    fun deleteProject(project: Project) {
        executor.execute { moneyMateDao.deleteProject(project) }
    }

    fun getTransactionsCountByCategory(category_id: UUID) {
        executor.execute { moneyMateDao.getTransactionsCountByCategory(category_id) }
    }

    fun getTransactionsCountByProject(project_id: UUID) {
        executor.execute { moneyMateDao.getTransactionsCountByProject(project_id) }
    }

    companion object {

        private var INSTANCE: MoneyMateRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MoneyMateRepository(context)
            }
        }

        fun get(): MoneyMateRepository {
            return INSTANCE ?: throw IllegalStateException("MoneyMateRepository must be init")
        }

    }
}