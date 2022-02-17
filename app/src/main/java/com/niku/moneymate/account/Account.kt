package com.niku.moneymate.account

import androidx.room.*
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.utils.UUID_CURRENCY_RUB
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(
    tableName = "account",
    foreignKeys = [
        ForeignKey(
            entity = MainCurrency::class,
            parentColumns = ["currency_id"],
            childColumns = ["currency_id"],
            onDelete = ForeignKey.SET_DEFAULT)
    ], indices = [Index("currency_id")]
)
data class Account(
    @PrimaryKey val account_id: UUID = UUID.randomUUID(),

    @NotNull
    @ColumnInfo(name = "currency_id", defaultValue = UUID_CURRENCY_RUB)
    var currency_id: UUID,

    var title: String = "",
    var initial_balance: Double = 0.0,
    var balance: Double = 0.0,
    var note: String = "",
    var is_active: Boolean = true,
    var is_include_into_totals: Boolean = true,
    var sort_order: Int = 0,
    val account_external_id: Int = 0
) {
    override fun toString(): String {
        return "$title $note"
    }
}