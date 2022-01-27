package com.niku.moneymate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.payee.Payee
import com.niku.moneymate.projects.Project
import com.niku.moneymate.transaction.MoneyTransaction

@Database(
    entities =
        [
            Account::class,
            MainCurrency::class,
            Category::class,
            MoneyTransaction::class,
            Project::class,
            Payee::class
        ]
    , version = 1, exportSchema = false)

@TypeConverters(MoneyMateTypeConverters::class)

abstract class MoneyMateDatabase: RoomDatabase() {
    abstract fun moneyMateDao(): MoneyMateDao
    abstract fun payeeDao(): PayeeDao
}