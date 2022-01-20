package com.niku.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
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
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        this.navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(this.navController)
/*
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.transactionListFragment, R.id.transactionListFragment)
                //.setDrawerLayout(drawerLayout)
                .build()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(
            R.id.transactionListFragment,
            R.id.accountListFragment,
            R.id.categoryListFragment,
            R.id.projectListFragment,
            R.id.currencyListFragment)).build()
        //setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)*/

        bottomNavigationView.setOnItemSelectedListener  {

            when(it.itemId) {

                R.id.budget -> {
                    navController.navigate(R.id.transactionListFragment)
                }
                R.id.accounts -> {
                    navController.navigate(R.id.accountListFragment)
                }
                R.id.categories->navController.navigate(R.id.categoryListFragment)
                R.id.projects->navController.navigate(R.id.projectListFragment)
                R.id.currencies->navController.navigate(R.id.currencyListFragment)

            }
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    override fun onAccountSelected(accountId: UUID) {
        navController.navigate(
                R.id.action_accountListFragment_to_accountFragment,
                AccountFragment.newBundle(accountId),
                navOptions { // Use the Kotlin DSL for building NavOptions
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
    }

    override fun onCurrencySelected(currencyId: UUID) {
        navController.navigate(
                R.id.action_currencyListFragment_to_currencyFragment,
                CurrencyFragment.newBundle(currencyId),
                navOptions { // Use the Kotlin DSL for building NavOptions
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
    }

    override fun onCategorySelected(categoryId: UUID) {
        navController.navigate(
                R.id.action_categoryListFragment_to_categoryFragment,
                CategoryFragment.newBundle(categoryId),
                navOptions { // Use the Kotlin DSL for building NavOptions
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
    }

    override fun onTransactionSelected(transactionId: UUID) {
        navController.navigate(
                R.id.action_transactionListFragment_to_transactionFragment,
                TransactionFragment.newBundle(transactionId),
                navOptions { // Use the Kotlin DSL for building NavOptions
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
    }

    override fun onProjectSelected(projectId: UUID) {
        navController.navigate(
                R.id.action_projectListFragment_to_projectFragment,
                ProjectFragment.newBundle(projectId),
                navOptions { // Use the Kotlin DSL for building NavOptions
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
    }

    override fun onSettingsSelected() {
        navController.navigate(
                R.id.action_transactionListFragment_to_mainSettingsFragment,
                MainSettingsFragment.newBundle(),
                navOptions { // Use the Kotlin DSL for building NavOptions
                        anim {
                            enter = android.R.animator.fade_in
                            exit = android.R.animator.fade_out
                        }
                    })
    }
}