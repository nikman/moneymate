package com.niku.moneymate

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.niku.moneymate.database.MoneyMateRepository

const val NOTIFICATION_CHANNEL_ID = "populate_db"

class MoneyMateApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // init db repository
        MoneyMateRepository.initialize(context = this)
        // init channel for notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}