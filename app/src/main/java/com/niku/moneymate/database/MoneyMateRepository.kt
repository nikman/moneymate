package com.niku.moneymate.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.niku.moneymate.account.Account
import com.niku.moneymate.Account
import com.niku.moneymate.category.Category
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "money-mate-database"

class MoneyMateRepository private constructor(context: Context) {

    private val database : MoneyMateDatabase = Room.databaseBuilder(
        context.applicationContext,
        MoneyMateDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    private val moneyMateDao = database.moneyMateDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAccounts(): LiveData<List<Account>> = moneyMateDao.getAccounts()
    //fun getCategories(): LiveData<List<Category>> = moneyMateDao.getCategories()

    fun getAccount(id: UUID): LiveData<Account?> = moneyMateDao.getAccount(id)

    fun updateAccount(account: Account) {
        executor.execute {
            moneyMateDao.updateAccount(account)
        }
    }

    fun addAccount(account: Account) {
        executor.execute {
            moneyMateDao.addAccount(account)
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