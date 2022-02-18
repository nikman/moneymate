package com.niku.moneymate.transaction

import androidx.room.Embedded
import androidx.room.Relation
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project

data class TransactionWithProperties(

    @Embedded
    val transaction: MoneyTransaction,

    @Relation(parentColumn = "account_id_from", entityColumn = "account_id")
    val accountFrom: Account,

    @Relation(parentColumn = "account_id_to", entityColumn = "account_id")
    val accountTo: Account,

    @Relation(parentColumn = "currency_id", entityColumn = "currency_id")
    val currency: MainCurrency,

    @Relation(parentColumn = "category_id", entityColumn = "category_id")
    val category: Category,

    @Relation(parentColumn = "project_id", entityColumn = "project_id")
    val project: Project

){//}: BaseListItem {
    override fun toString(): String = "${accountFrom.title}, amount: ${transaction.amount_from} ${transaction.note}"
}