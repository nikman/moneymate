package com.niku.moneymate

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.*

private const val PREF_LAST_ACCOUNT_ID = "lastAccountId"

object AppPreference {

    fun setLastAccountId(context: Context, lastAccountId: UUID) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString(PREF_LAST_ACCOUNT_ID, lastAccountId.toString())
        }
    }

    fun getLastAccountId(context: Context): UUID {

        return UUID.fromString(
            PreferenceManager
                .getDefaultSharedPreferences(context)
                    .getString(PREF_LAST_ACCOUNT_ID, ""))!!

    }

}