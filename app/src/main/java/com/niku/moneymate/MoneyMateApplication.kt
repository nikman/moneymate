package com.niku.moneymate

import android.app.Application
import com.niku.moneymate.database.MoneyMateRepository

class MoneyMateApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MoneyMateRepository.initialize(context = this)
    }
}