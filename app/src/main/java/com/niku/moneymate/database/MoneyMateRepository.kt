package com.niku.moneymate.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import java.util.*
import java.util.concurrent.Executors

const val DATABASE_NAME = "money-mate-database"

class MoneyMateRepository private constructor(context: Context) {

    private val database : MoneyMateDatabase = Room.databaseBuilder(
        context.applicationContext,
        MoneyMateDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build() // !

    private val moneyMateDao = database.moneyMateDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAccounts(): LiveData<List<AccountWithCurrency>> = moneyMateDao.getAccounts()
    fun getCurrencies(): LiveData<List<MainCurrency>> = moneyMateDao.getCurrencies()
    fun getCategories(): LiveData<List<Category>> = moneyMateDao.getCategories()

    fun getAccount(id: UUID): LiveData<AccountWithCurrency?> = moneyMateDao.getAccount(id)
    fun getCurrency(id: UUID): LiveData<MainCurrency?> = moneyMateDao.getCurrency(id)
    fun getCategory(id: UUID): LiveData<Category?> = moneyMateDao.getCategory(id)

    fun updateAccount(account: AccountWithCurrency) {
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

    fun addCategory(category: Category) {
        executor.execute {
            moneyMateDao.addCategory(category)
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