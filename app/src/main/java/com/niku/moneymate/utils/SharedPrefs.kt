package com.niku.moneymate.utils

import android.content.Context
import java.util.*

private const val KEY_NAME = "money-mate-shared-prefs-key"
private const val CURRENCY_PREFS_KEY = "currency-code-key"
private const val CATEGORY_PREFS_KEY = "category-code-key"
private const val ACCOUNT_PREFS_KEY = "account-code-key"
private const val PROJECT_PREFS_KEY = "project-code-key"

class SharedPrefs {

    fun storeCurrencyId(context: Context, value: UUID) {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(CURRENCY_PREFS_KEY, value.toString())
        editor.apply()

    }

    fun storeCategoryId(context: Context, value: UUID) {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(CATEGORY_PREFS_KEY, value.toString())
        editor.apply()

    }

    fun storeAccountId(context: Context, value: UUID) {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(ACCOUNT_PREFS_KEY, value.toString())
        editor.apply()

    }

    fun storeProjectId(context: Context, value: UUID) {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(PROJECT_PREFS_KEY, value.toString())
        editor.apply()

    }

    fun getStoredCurrencyId(context: Context): String? {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        return preferences.getString(CURRENCY_PREFS_KEY, UUID_CURRENCY_RUB)

    }

    fun getStoredCategoryId(context: Context): String? {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        return preferences.getString(CATEGORY_PREFS_KEY, UUID_CATEGORY_FOOD)

    }

    fun getStoredAccountId(context: Context): String? {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        return preferences.getString(ACCOUNT_PREFS_KEY, UUID_ACCOUNT_CASH)

    }

    fun getStoredProjectId(context: Context): String? {

        val preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PROJECT_PREFS_KEY, UUID_PROJECT_EMPTY)

    }

}