package com.niku.moneymate.utils

import android.content.Context
import java.util.*

private const val KEY_NAME = "money-mate-shared-prefs-key"
private const val CURRENCY_PREFS_KEY = "currency-code-key"

class SharedPrefs {

    fun storeCurrencyId(context: Context, value: UUID) {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(CURRENCY_PREFS_KEY, value.toString())
        editor.apply()

    }

    fun getStoredCurrencyId(context: Context): String? {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        return preferences.getString(CURRENCY_PREFS_KEY, "")

    }

}