package com.niku.moneymate.database

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.niku.moneymate.currency.MainCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File

//const val TAG = "SeedDatabaseWorker"

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Result.success()
        /*try {

            val filename = inputData.getString(KEY_FILENAME)
            Log.d(TAG, "File name: ${filename.toString()}")
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use {
                            jsonReader ->

                        val bufferedReader: BufferedReader = File(filename).bufferedReader()

                        val inputString = bufferedReader.use { it.readText() }
                        val gson = Gson()
                        val mainCurrencyType = object : TypeToken<List<MainCurrency>>() {}.type
                        val currencyList: List<MainCurrency> =
                            gson.fromJson(inputStream.toString(), mainCurrencyType)

                        Log.d(TAG, "Count from json: ${currencyList.size}")

                        MoneyMateRepository.get().insertAllCurrencies(currencyList)

                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }*/
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
        const val KEY_FILENAME = "CURRENCY_DATA_FILENAME"
        const val CURRENCY_DATA_FILENAME = "currencies.json"
    }
}
