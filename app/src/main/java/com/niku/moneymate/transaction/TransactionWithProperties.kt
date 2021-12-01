package com.niku.moneymate.transaction

import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import java.util.*

data class TransactionWithProperties(

    @Embedded
    val transaction: MoneyTransaction,

    @Relation(parentColumn = "account_id", entityColumn = "account_id")
    val account: Account,

    @Relation(parentColumn = "currency_id", entityColumn = "currency_id")
    val currency: MainCurrency,

    @Relation(parentColumn = "category_id", entityColumn = "category_id")
    val category: Category

)