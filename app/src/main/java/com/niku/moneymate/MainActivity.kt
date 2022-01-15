package com.niku.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.niku.moneymate.projects.Project
import com.niku.moneymate.ui.main.account.AccountFragment
import com.niku.moneymate.ui.main.account.AccountListFragment
import com.niku.moneymate.ui.main.category.CategoryFragment
import com.niku.moneymate.ui.main.category.CategoryListFragment
import com.niku.moneymate.ui.main.currency.CurrencyFragment
import com.niku.moneymate.ui.main.currency.CurrencyListFragment
import com.niku.moneymate.ui.main.project.ProjectFragment
import com.niku.moneymate.ui.main.project.ProjectListFragment
import com.niku.moneymate.ui.main.settings.MainSettingsFragment
import com.niku.moneymate.ui.main.transaction.TransactionFragment
import com.niku.moneymate.ui.main.transaction.TransactionListFragment
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

class MainActivity :
    AppCompatActivity(),
    AccountListFragment.Callbacks,
    CurrencyListFragment.Callbacks,
    CategoryListFragment.Callbacks,
    TransactionListFragment.Callbacks,
    ProjectListFragment.Callbacks {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(
            R.id.transactionListFragment,
            R.id.accountListFragment,
            R.id.categoryListFragment,
            R.id.projectListFragment,
            R.id.currencyListFragment)).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener  {

            when(it.itemId) {

                R.id.budget -> {
                    navController.navigate(R.id.transactionListFragment)
                    //navController.clearBackStack(R.id.transactionListFragment)
                    //navController.popBackStack()
                }
                R.id.accounts -> {
                    navController.navigate(R.id.accountListFragment)
                    //navController.clearBackStack(R.id.accountListFragment)
                    //navController.popBackStack()
                }
                R.id.categories->navController.navigate(R.id.categoryListFragment)
                R.id.projects->navController.navigate(R.id.projectListFragment)
                R.id.currencies->navController.navigate(R.id.currencyListFragment)

            }
            true
        }

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data
            selectedFile?.let {
                Log.d(TAG, it.toString())
                val filePath: String? = selectedFile.path
                val segment = selectedFile.lastPathSegment
                segment?.let { Log.d(TAG, segment) }
                filePath?.let {
                    Log.d(TAG, filePath)
                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        com.niku.moneymate.files.FileUtils().readFileLineByLine(filePath)
                    }
                }
            }
        }
    }*/

    override fun onAccountSelected(accountId: UUID) {
        navController.navigate(
                R.id.action_accountListFragment_to_accountFragment,
                AccountFragment.newBundle(accountId))
    }

    override fun onCurrencySelected(currencyId: UUID) {
        //findNavController(this, R.id.nav_host_fragment).
        navController.navigate(
                R.id.action_currencyListFragment_to_currencyFragment,
                CurrencyFragment.newBundle(currencyId))
    }

    override fun onCategorySelected(categoryId: UUID) {
        //findNavController(this, R.id.nav_host_fragment).
        navController.navigate(
                R.id.action_categoryListFragment_to_categoryFragment,
                CategoryFragment.newBundle(categoryId))
    }

    override fun onTransactionSelected(transactionId: UUID) {
        //findNavController(this, R.id.nav_host_fragment).
        navController.navigate(
                R.id.action_transactionListFragment_to_transactionFragment,
                TransactionFragment.newBundle(transactionId))
    }

    override fun onProjectSelected(projectId: UUID) {
        //findNavController(this, R.id.nav_host_fragment).
        navController.navigate(
                R.id.action_projectListFragment_to_projectFragment,
                ProjectFragment.newBundle(projectId))
    }

    override fun onSettingsSelected() {
        //findNavController(this, R.id.nav_host_fragment).
        navController.navigate(
                R.id.action_transactionListFragment_to_mainSettingsFragment,
                MainSettingsFragment.newBundle())
    }
}