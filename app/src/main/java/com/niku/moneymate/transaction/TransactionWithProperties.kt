package com.niku.moneymate.transaction

import androidx.room.Embedded
import androidx.room.Relation
import com.niku.moneymate.account.Account
import com.niku.moneymate.currency.MainCurrency

data class TransactionWithProperties(
    @Embedded
    val transaction: MoneyTransaction,

    @Relation(parentColumn = "account_id", entityColumn = "account_id")
    val account: Account,

    @Relation(parentColumn = "currency_id", entityColumn = "currency_id")
    val currency: MainCurrency
)