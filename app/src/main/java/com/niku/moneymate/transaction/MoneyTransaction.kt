package com.niku.moneymate.transaction

import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import java.time.Clock
import java.time.LocalDate
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
            childColumns = ["currency_id"]),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"])
    ],
    indices = [
        Index("account_id"),
        Index("currency_id"),
        Index("category_id")
    ]
)
data class MoneyTransaction(

    var account_id: UUID,
    var currency_id: UUID,
    var category_id: UUID,

    @ColumnInfo(name = "transaction_date") var transactionDate: Date = Date(),
    @ColumnInfo(name = "amount") var amount: Double = 0.0,
    @ColumnInfo(name = "transaction_id") @PrimaryKey val transaction_id: UUID = UUID.randomUUID(),
    var posted: Boolean = false
)