package com.niku.moneymate.transaction

import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.currency.MainCurrency
import java.util.*

@Entity(
    tableName = "moneyTransaction",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"]),
        ForeignKey(
            entity = MainCurrency::class,
            parentColumns = ["currency_id"],
            childColumns = ["currency_id"])
    ], indices = [Index("account_id"),Index("currency_id")]
)
data class MoneyTransaction(

    @Relation(parentColumn = "account_id", entityColumn = "account_id")
    val account_id: UUID,

    @Relation(parentColumn = "currency_id", entityColumn = "currency_id")
    val currency_id: UUID,

    var amount: Double = 0.0,

    val transaction_id: UUID = UUID.randomUUID()
)