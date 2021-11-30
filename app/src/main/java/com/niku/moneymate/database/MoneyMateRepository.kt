package com.niku.moneymate.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionWithProperties
import java.util.*
import java.util.concurrent.Executors

const val DATABASE_NAME = "money-mate-database"
const val TAG = "MoneyMateRepository"

class MoneyMateRepository private constructor(context: Context) {

    //private val context = context

    private val database : MoneyMateDatabase = Room.databaseBuilder(
            context.applicationContext,
            MoneyMateDatabase::class.java,
            DATABASE_NAME)
        /*.addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d(TAG, "onCreate DB")
                    val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                        .setInputData(workDataOf(SeedDatabaseWorker.KEY_FILENAME to SeedDatabaseWorker.CURRENCY_DATA_FILENAME))
                        .build()
                    WorkManager.getInstance(context).enqueue(request)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d(TAG, "onOpen database")
                }
            })*/
        .fallbackToDestructiveMigration()
        .build() // !

    private val moneyMateDao = database.moneyMateDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAccounts(): LiveData<List<AccountWithCurrency>> = moneyMateDao.getAccounts()
    fun getCurrencies(): LiveData<List<MainCurrency>> = moneyMateDao.getCurrencies()
    fun getCategories(): LiveData<List<Category>> = moneyMateDao.getCategories()
    fun getTransactions(): LiveData<List<TransactionWithProperties>> = moneyMateDao.getTransactions()

    fun getAccount(id: UUID): LiveData<AccountWithCurrency?> = moneyMateDao.getAccount(id)
    fun getCurrency(id: UUID): LiveData<MainCurrency?> = moneyMateDao.getCurrency(id)
    //fun getDefaultCurrency(): LiveData<MainCurrency?> = moneyMateDao.getDefaultCurrency()
    fun getCategory(id: UUID): LiveData<Category?> = moneyMateDao.getCategory(id)
    fun getTransaction(id: UUID): LiveData<TransactionWithProperties?> = moneyMateDao.getTransaction(id)

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

    companion object{

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