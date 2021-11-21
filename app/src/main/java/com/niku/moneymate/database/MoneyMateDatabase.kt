package com.niku.moneymate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niku.moneymate.Account
import com.niku.moneymate.Currency
import com.niku.moneymate.category.Category

@Database(entities = [Account::class,Currency::class,Category::class], version = 3)
@TypeConverters(MoneyMateTypeConverters::class)

abstract class MoneyMateDatabase: RoomDatabase() {
    abstract fun moneyMateDao(): MoneyMateDao
}