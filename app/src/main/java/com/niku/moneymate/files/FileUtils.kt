package com.niku.moneymate.files

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.niku.moneymate.account.AccountDetailViewModel
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.database.MoneyMateRepository
import com.niku.moneymate.payee.Payee
import com.niku.moneymate.projects.Project
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.utils.CategoryType
import com.niku.moneymate.utils.SharedPrefs
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

private const val TAG = "FileUtils"

class FileUtils {

    fun Context.launchFileIntent(filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(this, packageName, File(filePath))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }

    fun readFileFromAssetsLineByLine(context: Context) {
        Executors.newSingleThreadExecutor().apply {
            val text = AssetsLoader.loadTextFromAsset(context, "db_backup")
            AssetsLoader.getAccountsDataFromFile(context, text)
        }
    }

    object AssetsLoader {

        fun loadTextFromAsset(context: Context, file: String): String {
            return context.assets.open(file).bufferedReader().use { reader ->
                reader.readText()
            }
        }

        fun getAccountsDataFromFile(context: Context, text: String) {

            val accountList: ArrayList<com.niku.moneymate.account.Account> = ArrayList()
            var isAccountLoading = false

            var accountTitle = ""
            var accountNote = ""
            var isActive = true
            var isIncludeIntoTotals = false
            var sortOrder = 0

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

            for (line in text.lines()) {

                if (isAccountLoading) {
                    when (line.substringBefore(":")) {
                        "title" -> accountTitle = line.substringAfter(":")
                        "number" -> accountNote = line.substringAfter(":")
                        "is_active" -> isActive = line.substringAfter(":") == "1"
                        "is_include_into_totals" -> isIncludeIntoTotals = line.substringAfter(":") == "1"
                        "sort_order"  -> sortOrder = line.substringAfter(":").toInt()
                    }
                } else if (isCategoryLoading) {
                    if (line.substringBefore(":") == "title") {
                        categoryTitle = line.substringAfter(":")
                    } else if (line.substringBefore(":") == "type") {
                        categoryType =
                            if (line.substringAfter(":") == "0") CategoryType.OUTCOME
                            else CategoryType.INCOME
                    }
                } else if (isPayeeLoading) {
                    if (line.substringBefore(":") == "title") {
                        payeeTitle = line.substringAfter(":")
                    } else if (line.substringBefore(":") == "is_active") {
                        payeeIsActive = line.substringAfter(":") == "1"
                    }
                } else if (isProjectLoading) {
                    if (line.substringBefore(":") == "title") {
                        projectTitle = line.substringAfter(":")
                    } else if (line.substringBefore(":") == "is_active") {
                        projectIsActive = line.substringAfter(":") == "1"
                    }
                }

                when (line) {
                    "\$ENTITY:account" -> isAccountLoading = true
                    "\$ENTITY:category" -> isCategoryLoading = true
                    "\$ENTITY:payee" -> isPayeeLoading = true
                    "\$ENTITY:project" -> isProjectLoading = true
                }

                if (line == "\$\$" && isAccountLoading) {

                    isAccountLoading = false

                    val account: com.niku.moneymate.account.Account =
                        com.niku.moneymate.account.Account(
                            currency_id = UUID.fromString(SharedPrefs().getStoredCurrencyId(context)),
                            title = accountTitle,
                            note = accountNote,
                            is_active = isActive,
                            is_include_into_totals = isIncludeIntoTotals,
                            sort_order = sortOrder)

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
                            category_title = categoryTitle)

                    categoryList.add(category)

                    categoryTitle = ""
                    categoryType = 0
                }

                if (line == "\$\$" && isPayeeLoading) {

                    isPayeeLoading = false

                    val payee =
                        Payee(
                            payee_title = payeeTitle,
                            is_active = payeeIsActive)

                    payeeList.add(payee)

                }

                if (line == "\$\$" && isProjectLoading) {

                    isProjectLoading = false

                    val project =
                        Project(
                            project_title = projectTitle,
                            is_active = projectIsActive)

                    projectList.add(project)

                }
            }

            //Executors.newSingleThreadExecutor().apply {
            //    this.execute {
                    val moneyMateRepository = MoneyMateRepository.get()
                    for (account in accountList) {
                        moneyMateRepository.addAccount(account)
                    }
            //    }
           // }

            //Executors.newSingleThreadExecutor().apply {
            //    this.execute {
             //       val moneyMateRepository = MoneyMateRepository.get()
                    for (category in categoryList) {
                        moneyMateRepository.addCategory(category)
                    }
             //   }
           // }
            //Executors.newSingleThreadExecutor().apply {
             //   this.execute {
             //       val moneyMateRepository = MoneyMateRepository.get()
                    for (payee in payeeList) {
                        moneyMateRepository.addPayee(payee)
                    }
            //    }
            //}
            //Executors.newSingleThreadExecutor().apply {
             //   this.execute {
             //       val moneyMateRepository = MoneyMateRepository.get()
                    for (project in projectList) {
                        moneyMateRepository.addProject(project)
                    }
              //  }
           // }

            // $ENTITY:transactions
            val transactionList: ArrayList<MoneyTransaction> = ArrayList()
            var isTransactionLoading = false

            var from_account_id = 0
            var to_account_id = 0
            var category_id = 0
            var project_id = 0
            var from_amount = 0

            for (line in text.lines()) {

                if (isTransactionLoading) {
                    when (line.substringBefore(":")) {
                        "from_account_id" -> from_account_id = line.substringAfter(":").toInt()
                        "to_account_id" -> to_account_id = line.substringAfter(":").toInt()
                        "category_id" -> category_id = line.substringAfter(":").toInt()
                        "project_id" -> project_id = line.substringAfter(":").toInt()
                        "from_amount"  -> from_amount = line.substringAfter(":").toInt()
                    }
                }

                when (line) {
                    "\$ENTITY:transactions" -> isTransactionLoading = true
                }

                if (line == "\$\$" && isTransactionLoading) {

                    isTransactionLoading = false

                    /*val transaction =
                        MoneyTransaction(
                            currency_id = UUID.fromString(SharedPrefs().getStoredCurrencyId(context)),
                            account_id_from = accountTitle,
                            account_id_to = accountNote,
                            category_id = isActive,
                            project_id = isIncludeIntoTotals,
                            transactionDate = sortOrder,
                            amount_from,
                            amount_to,
                            note,
                            transaction_type)

                    transactionList.add(transaction)*/

                }
            }
            for (transaction in transactionList) {
                moneyMateRepository.addTransaction(transaction)
            }
        }
    }
}