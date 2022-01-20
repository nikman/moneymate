package com.niku.moneymate.files

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.database.MoneyMateRepository
import com.niku.moneymate.database.SeedDatabaseWorker
import com.niku.moneymate.database.SeedDatabaseWorker.Companion.KEY_FILENAME
import com.niku.moneymate.payee.Payee
import com.niku.moneymate.projects.Project
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.utils.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "FileUtils"

class FileUtils {

    infix fun <A, B> A.into(that: B): Pair<A, B> = Pair(this, that)

    fun Context.launchFileIntent(filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(this, packageName, File(filePath))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }

    fun readFileFromAssetsLineByLine(context: Context): Unit {

        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
            .setInputData(workDataOf(KEY_FILENAME into "database/20220120_223208_566"))
            .build()
        WorkManager.getInstance(context).enqueue(request)

    }

    object AssetsLoader {

        fun loadTextFromAsset(context: Context, file: String): String {
            return context.assets.open(file).bufferedReader().use { reader ->
                reader.readText()
            }
        }

        fun getAccountsDataFromFile(text: String) {

            val accountList: ArrayList<Account> = ArrayList()
            var isAccountLoading = false

            var accountTitle = ""
            var accountNote = ""
            var isActive = true
            var isIncludeIntoTotals = false
            var sortOrder = 0
            var accountCurrencyId = ""

            val categoryList: ArrayList<Category> = ArrayList()
            var isCategoryLoading = false

            var categoryType = 0
            var categoryTitle = ""

            // $ENTITY:payee
            val payeeList: ArrayList<Payee> = ArrayList()
            var isPayeeLoading = false

            var payeeTitle = ""
            var payeeIsActive = true

            // $ENTITY:project
            val projectList: ArrayList<Project> = ArrayList()
            var isProjectLoading = false

            var projectTitle = ""
            var projectIsActive = true

            var payee_external_id:  Int = 0
            var account_external_id:  Int = 0
            var category_external_id:  Int = 0
            var project_external_id:  Int = 0

                for (line in text.lines()) {

                    if (isAccountLoading) {
                        when (line.substringBefore(":")) {
                            "title" -> accountTitle = line.substringAfter(":")
                            "number" -> accountNote = line.substringAfter(":")
                            "is_active" -> isActive = line.substringAfter(":") == "1"
                            "is_include_into_totals" -> isIncludeIntoTotals =
                                line.substringAfter(":") == "1"
                            "sort_order" -> sortOrder = line.substringAfter(":").toInt()
                            "currency_id" -> accountCurrencyId = line.substringAfter(":")
                            "_id" -> account_external_id = line.substringAfter(":").toInt()
                        }
                    } else if (isCategoryLoading) {
                        if (line.substringBefore(":") == "title") {
                            categoryTitle = line.substringAfter(":")
                        } else if (line.substringBefore(":") == "type") {
                            categoryType =
                                if (line.substringAfter(":") == "0") CategoryType.OUTCOME
                                else CategoryType.INCOME
                        } else if (line.substringBefore(":") == "_id") { category_external_id = line.substringAfter(":").toInt() }
                    } else if (isPayeeLoading) {
                        if (line.substringBefore(":") == "title") {
                            payeeTitle = line.substringAfter(":")
                        } else if (line.substringBefore(":") == "is_active") {
                            payeeIsActive = line.substringAfter(":") == "1"
                        } else if (line.substringBefore(":") == "_id") { payee_external_id = line.substringAfter(":").toInt() }
                    } else if (isProjectLoading) {
                        if (line.substringBefore(":") == "title") {
                            projectTitle = line.substringAfter(":")
                        } else if (line.substringBefore(":") == "is_active") {
                            projectIsActive = line.substringAfter(":") == "1"
                        } else if (line.substringBefore(":") == "_id") { project_external_id = line.substringAfter(":").toInt() }
                    }

                    when (line) {
                        "\$ENTITY:account" -> isAccountLoading = true
                        "\$ENTITY:category" -> isCategoryLoading = true
                        "\$ENTITY:payee" -> isPayeeLoading = true
                        "\$ENTITY:project" -> isProjectLoading = true
                    }

                    if (line == "\$\$" && isAccountLoading) {

                        isAccountLoading = false

                        //$ENTITY:currency
                        //_id:1
                        //name:RUB
                        //$ENTITY:currency
                        //_id:2
                        //name:USD

                        val account: Account =
                            Account(
                                currency_id = when (accountCurrencyId) {
                                    "1" -> UUID.fromString(UUID_CURRENCY_RUB)
                                    "2" -> UUID.fromString(UUID_CURRENCY_USD)
                                    else -> UUID.fromString(UUID_CURRENCY_RUB)
                                },
                                title = accountTitle,
                                note = accountNote,
                                is_active = isActive,
                                is_include_into_totals = isIncludeIntoTotals,
                                sort_order = sortOrder,
                                account_external_id = account_external_id
                            )

                        accountList.add(account)

                        accountTitle = ""
                        accountNote = ""
                        isActive = true
                        isIncludeIntoTotals = false
                        sortOrder = 0
                    }

                    if (line == "\$\$" && isCategoryLoading) {

                        isCategoryLoading = false

                        val category =
                            Category(
                                category_type = categoryType,
                                category_title = categoryTitle,
                                category_external_id = category_external_id
                            )

                        categoryList.add(category)

                        categoryTitle = ""
                        categoryType = 0
                    }

                    if (line == "\$\$" && isPayeeLoading) {

                        isPayeeLoading = false

                        val payee =
                            Payee(
                                payee_title = payeeTitle,
                                is_active = payeeIsActive,
                                payee_external_id = payee_external_id
                            )

                        payeeList.add(payee)

                    }

                    if (line == "\$\$" && isProjectLoading) {

                        isProjectLoading = false

                        val project =
                            Project(
                                project_title = projectTitle,
                                is_active = projectIsActive,
                                project_external_id = project_external_id
                            )

                        projectList.add(project)

                    }
                }

                val moneyMateRepository = MoneyMateRepository.get()

                for (account in accountList) {
                    moneyMateRepository.addAccount(account)
                }

                for (category in categoryList) {
                    moneyMateRepository.addCategory(category)
                }

                for (payee in payeeList) {
                    moneyMateRepository.addPayee(payee)
                }

                for (project in projectList) {
                    moneyMateRepository.addProject(project)
                }

                // $ENTITY:transactions
                val transactionList: ArrayList<MoneyTransaction> = ArrayList()
                var isTransactionLoading = false

                var from_account_id = 0
                var to_account_id = 0
                var category_id = 0
                var project_id = 0
                var from_amount = 0
                var to_amount = 0
                var datetime = 0L
                var payee_id = 0
                var transaction_note = ""

                var accountUUIDFrom: UUID?
                var accountUUIDTo: UUID?
                var accountFrom: Account?
                var accountTo: Account
                var categoryUUID: UUID?
                var projectUUID: UUID?
                var category: Category? = null
                var transaction_type: Int

                //var trCount = 0

                for (line in text.lines()) {

                    /*if (trCount > 300) { break }
                    else {  }*/

                    if (isTransactionLoading) {
                        when (line.substringBefore(":")) {
                            "from_account_id" -> from_account_id = line.substringAfter(":").toInt()
                            "to_account_id" -> to_account_id = line.substringAfter(":").toInt()
                            "category_id" -> category_id = line.substringAfter(":").toInt()
                            "project_id" -> project_id = line.substringAfter(":").toInt()
                            "from_amount" -> from_amount = line.substringAfter(":").toInt()
                            "to_amount" -> to_amount = line.substringAfter(":").toInt()
                            "datetime" -> datetime = line.substringAfter(":").toLong()
                            "payee_id" -> payee_id = line.substringAfter(":").toInt()
                            "note" -> transaction_note = line.substringAfter(":")
                        }
                    }

                    if (line == "\$ENTITY:transactions") {
                        isTransactionLoading = true
                        //trCount++
                    } else if (line == "\$\$" && isTransactionLoading) {

                        isTransactionLoading = false

                        accountUUIDFrom =
                            moneyMateRepository.getAccountByExternalId(from_account_id)
                        accountUUIDTo = moneyMateRepository.getAccountByExternalId(to_account_id)
                        categoryUUID = moneyMateRepository.getCategoryByExternalId(category_id)
                        projectUUID = moneyMateRepository.getProjectByExternalId(project_id)

                        if (accountUUIDFrom != null) {

                            accountFrom = moneyMateRepository.getAccountDirect(accountUUIDFrom)
                            if (categoryUUID != null && categoryUUID != UUID.fromString(UUID_CATEGORY_EMPTY)) {
                                category = moneyMateRepository.getCategoryDirect(categoryUUID)
                            }

                            if (accountFrom != null) {

                                if (category != null) {
                                    if (from_amount > 0 && category.category_type == CategoryType.OUTCOME)
                                        transaction_type = TransactionType.INCOME
                                    else if (from_amount < 0 && category.category_type == CategoryType.INCOME)
                                        transaction_type = TransactionType.OUTCOME
                                    else transaction_type = category.category_type
                                } else {
                                    transaction_type = if (from_amount > 0) TransactionType.INCOME else TransactionType.OUTCOME
                                }

                                val transaction =
                                    MoneyTransaction(
                                        currency_id = accountFrom.currency_id,
                                        account_id_from = accountUUIDFrom,
                                        account_id_to = accountUUIDTo ?: UUID.fromString(UUID_ACCOUNT_EMPTY),
                                        category_id = categoryUUID ?: UUID.fromString(UUID_CATEGORY_EMPTY),
                                        project_id = projectUUID ?: UUID.fromString(UUID_PROJECT_EMPTY),
                                        transactionDate = Date(datetime),
                                        amount_from = (from_amount / 100).toDouble(),
                                        amount_to = (to_amount / 100).toDouble(),
                                        note = transaction_note,
                                        transaction_type = transaction_type
                                    )

                                transactionList.add(transaction)
                            }

                        }

                        from_account_id = 0
                        to_account_id = 0
                        category_id = 0
                        project_id = 0
                        from_amount = 0
                        to_amount = 0
                        datetime = 0
                        payee_id = 0
                        transaction_note = ""
                        transaction_type = 0

                    }
                }
                for (transaction in transactionList) {
                    moneyMateRepository.addTransaction(transaction)
                }
        }
    }
}