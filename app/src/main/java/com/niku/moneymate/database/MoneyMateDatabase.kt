package com.niku.moneymate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.niku.moneymate.account.Account
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.category.Category

@Database(entities = [Account::class, MainCurrency::class,Category::class], version = 3)
@TypeConverters(MoneyMateTypeConverters::class)

abstract class MoneyMateDatabase: RoomDatabase() {
    abstract fun moneyMateDao(): MoneyMateDao

    /*companion object {
        private fun buildDatabase(context: Context): MoneyMateDatabase {
            return Room.databaseBuilder(context, MoneyMateDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }*/

}