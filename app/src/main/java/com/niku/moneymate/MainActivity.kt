package com.niku.moneymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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

private const val TAG = "MainActivity"

class MainActivity :
    AppCompatActivity(),
    AccountListFragment.Callbacks,
    CurrencyListFragment.Callbacks,
    CategoryListFragment.Callbacks,
    TransactionListFragment.Callbacks,
    ProjectListFragment.Callbacks,
    MainSettingsFragment.Callbacks
{

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        this.navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(this.navController)

        bottomNavigationView.setOnItemSelectedListener  {

            when(it.itemId) {

                R.id.budget -> {
                    navController.navigate(R.id.transactionListFragment)
                }
                R.id.accounts -> {
                    navController.navigate(R.id.accountListFragment)
                }
                R.id.categories-> {
                    navController.navigate(R.id.categoryListFragment)
                }
                R.id.projects-> {
                    navController.navigate(R.id.projectListFragment)
                }
                R.id.currencies-> {
                    navController.navigate(R.id.currencyListFragment)
                }

            }
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    override fun onAccountSelected(accountId: UUID) {
        navigateByAction(
            actionId = R.id.action_accountListFragment_to_accountFragment,
            bundle = AccountFragment.newBundle(accountId))
    }

    override fun onCurrencySelected(currencyId: UUID) {
        navigateByAction(
            actionId = R.id.action_currencyListFragment_to_currencyFragment,
            bundle = CurrencyFragment.newBundle(currencyId))
    }

    override fun onCategorySelected(categoryId: UUID) {
        navigateByAction(
            actionId = R.id.action_categoryListFragment_to_categoryFragment,
            bundle = CategoryFragment.newBundle(categoryId))
    }

    override fun onTransactionSelected(transactionId: UUID) {
        navigateByAction(
            actionId = R.id.action_transactionListFragment_to_transactionFragment,
            bundle = TransactionFragment.newBundle(transactionId))
    }

    override fun onProjectSelected(projectId: UUID) {
        navigateByAction(
            actionId = R.id.action_projectListFragment_to_projectFragment,
            bundle = ProjectFragment.newBundle(projectId))
    }

    override fun onSettingsSelected() {
        navigateByAction(
            actionId = R.id.action_transactionListFragment_to_mainSettingsFragment,
            bundle = MainSettingsFragment.newBundle())
    }

    override fun onCurrencyListFromPreferencesOpenSelected() {
        navigateByAction(
            actionId = R.id.action_mainSettingsFragment_to_currencyListFragment,
            bundle = null)
    }

    override fun onProjectListFromPreferencesOpenSelected() {
        navigateByAction(
            actionId = R.id.action_mainSettingsFragment_to_projectListFragment,
            bundle = null)
    }

    private fun navigateByAction(actionId: Int, bundle: Bundle?) {
        navController.navigate(
            actionId,
            bundle,
            navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = android.R.animator.fade_in
                    exit = android.R.animator.fade_out
                }
            })
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}