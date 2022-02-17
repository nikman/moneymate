package com.niku.moneymate.transaction

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project
import com.niku.moneymate.utils.UUID_ACCOUNT_EMPTY
import com.niku.moneymate.utils.UUID_CATEGORY_EMPTY
import com.niku.moneymate.utils.UUID_CURRENCY_RUB
import com.niku.moneymate.utils.UUID_PROJECT_EMPTY
import java.util.*

@Entity(
    tableName = "moneyTransaction",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id_from"],
            onDelete = ForeignKey.SET_DEFAULT),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id_to"],
            onDelete = ForeignKey.SET_DEFAULT),
        ForeignKey(
            entity = MainCurrency::class,
            parentColumns = ["currency_id"],
            childColumns = ["currency_id"],
            onDelete = ForeignKey.SET_DEFAULT),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_DEFAULT),
        ForeignKey(
            entity = Project::class,
            parentColumns = ["project_id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.SET_DEFAULT)
    ],
    indices = [
        Index("account_id_from"),
        Index("account_id_to"),
        Index("currency_id"),
        Index("category_id"),
        Index("project_id")
    ]
)
data class MoneyTransaction(

    @NonNull
    @ColumnInfo(name = "account_id_from", defaultValue = UUID_ACCOUNT_EMPTY)
    var account_id_from: UUID = UUID.fromString(UUID_ACCOUNT_EMPTY),

    @NonNull
    @ColumnInfo(name = "account_id_to", defaultValue = UUID_ACCOUNT_EMPTY)
    var account_id_to: UUID = UUID.fromString(UUID_ACCOUNT_EMPTY),

    @NonNull
    @ColumnInfo(name = "currency_id", defaultValue = UUID_CURRENCY_RUB)
    var currency_id: UUID = UUID.fromString(UUID_CURRENCY_RUB),

    @NonNull
    @ColumnInfo(name = "category_id", defaultValue = UUID_CATEGORY_EMPTY)
    var category_id: UUID = UUID.fromString(UUID_CATEGORY_EMPTY),

    @Nullable
    @ColumnInfo(name = "project_id", defaultValue = UUID_PROJECT_EMPTY)
    var project_id: UUID? = UUID.fromString(UUID_PROJECT_EMPTY),

    @ColumnInfo(name = "transaction_date") var transactionDate: Date = Date(),
    @ColumnInfo(name = "amount_from") var amount_from: Double = 0.0,
    @ColumnInfo(name = "amount_to") var amount_to: Double = 0.0,
    @ColumnInfo(name = "transaction_id") @PrimaryKey val transaction_id: UUID = UUID.randomUUID(),
    var note: String = "",
    var posted: Boolean = false,
    var transaction_type: Int = 0
)