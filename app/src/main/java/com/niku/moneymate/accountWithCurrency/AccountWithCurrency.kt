package com.niku.moneymate.accountWithCurrency

import androidx.room.Embedded
import androidx.room.Relation
import com.niku.moneymate.account.Account
import com.niku.moneymate.currency.MainCurrency

data class AccountWithCurrency(
    @Embedded
    val account: Account,

    /*@Embedded
    val currency: MainCurrency,*/

    @Relation(parentColumn = "currency_id", entityColumn = "currency_id")
    val currency: MainCurrency
)