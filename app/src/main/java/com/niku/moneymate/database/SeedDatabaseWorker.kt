package com.niku.moneymate.database

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.niku.moneymate.MainActivity
import com.niku.moneymate.NOTIFICATION_CHANNEL_ID
import com.niku.moneymate.R
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.files.FileUtils
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
        //Result.success()
        try {

            val filename = inputData.getString(KEY_FILENAME)
            Log.d(TAG, "File name: ${filename.toString()}")
            if (filename != null) {
                val text = FileUtils.AssetsLoader.loadTextFromAsset(applicationContext, filename)
                FileUtils.AssetsLoader.getAccountsDataFromFile(text)


                val intent = MainActivity.newIntent(applicationContext)
                val pendingIntent = PendingIntent.getActivity(
                    applicationContext, 0, intent, 0)
                val resources = applicationContext.resources
                val notification = NotificationCompat
                    .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                    .setTicker(resources.getString(R.string.notification_channel_name))
                    .setSmallIcon(R.drawable.ic_action_navbar_item_account)
                    .setContentTitle(resources.getString(R.string.notification_caption))
                    .setContentText(resources.getString(R.string.notification_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(0, notification)
                }

                Result.success()
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
        const val KEY_FILENAME = "FINANCISTO_DATA_FILENAME"
        //const val CURRENCY_DATA_FILENAME = "currencies.json"
    }
}
