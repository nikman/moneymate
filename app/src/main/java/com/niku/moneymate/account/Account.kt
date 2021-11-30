package com.niku.moneymate.account

import androidx.room.*
import com.niku.moneymate.currency.MainCurrency
import java.util.*

@Entity(
    tableName = "account",
    foreignKeys = [
        ForeignKey(
            entity = MainCurrency::class,
            parentColumns = ["currency_id"],
            childColumns = ["currency_id"])
    ], indices = [Index("currency_id")]
)
data class Account(
    var currency_id: UUID,
    var title: String = "",
    var balance: Int = 0,
    var note: String = "",
    @PrimaryKey val account_id: UUID = UUID.randomUUID()
)