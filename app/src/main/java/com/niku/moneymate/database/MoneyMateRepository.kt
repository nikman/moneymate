package com.niku.moneymate.database

import android.content.Context
import androidx.room.Room
import com.niku.moneymate.Account
import java.util.*

private const val DATABASE_NAME = "money-mate-database"

class MoneyMateRepository private constructor(context: Context) {

    private val database : MoneyMateDatabase = Room.databaseBuilder(
        context.applicationContext,
        MoneyMateDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val moneyMateDao = database.accountDao()

    fun getAccounts(): List<Account> = moneyMateDao.getAccounts()

    fun getAccount(id: UUID): Account? = moneyMateDao.getAccount(id)

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