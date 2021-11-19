package com.niku.moneymate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niku.moneymate.Account
import com.niku.moneymate.Currency

@Database(entities = [Account::class,Currency::class], version = 2)
@TypeConverters(MoneyMateTypeConverters::class)

abstract class MoneyMateDatabase: RoomDatabase() {
    abstract fun accountDao(): MoneyMateDao
}