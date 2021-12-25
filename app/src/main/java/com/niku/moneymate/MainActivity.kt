package com.niku.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.niku.moneymate.ui.main.account.AccountFragment
import com.niku.moneymate.ui.main.account.AccountListFragment
import com.niku.moneymate.ui.main.category.CategoryFragment
import com.niku.moneymate.ui.main.category.CategoryListFragment
import com.niku.moneymate.ui.main.currency.CurrencyFragment
import com.niku.moneymate.ui.main.currency.CurrencyListFragment
import com.niku.moneymate.ui.main.transaction.TransactionFragment
import com.niku.moneymate.ui.main.transaction.TransactionListFragment
import java.util.*

class MainActivity :
    AppCompatActivity(),
    AccountListFragment.Callbacks,
    CurrencyListFragment.Callbacks,
    CategoryListFragment.Callbacks,
    TransactionListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        /*if (savedInstanceState == null) {
            setCurrentFragment(TransactionListFragment.newInstance())
        }*/
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        /*bottomNavigationView.setOnItemSelectedListener  {

            when(it.itemId) {

                R.id.budget->setCurrentFragment(TransactionListFragment.newInstance())
                R.id.accounts->setCurrentFragment(AccountListFragment.newInstance())
                R.id.categories->setCurrentFragment(CategoryListFragment.newInstance())
                R.id.projects->setCurrentFragment(CategoryListFragment.newInstance())
                R.id.currencies->setCurrentFragment(CurrencyListFragment.newInstance())

            }
            true
        }*/
        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        val navController: NavController = navHostFragment.navController*/

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(
            R.id.transactionListFragment,
            R.id.accountListFragment,
            R.id.categoryListFragment,
            R.id.currencyListFragment)).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener  {

            when(it.itemId) {

                R.id.budget->navController.navigate(R.id.transactionListFragment)
                R.id.accounts->navController.navigate(R.id.accountListFragment)
                R.id.categories->navController.navigate(R.id.categoryListFragment)
                R.id.projects->navController.navigate(R.id.categoryListFragment) // todo
                R.id.currencies->navController.navigate(R.id.currencyListFragment)

            }
            true
        }

    }

    override fun onAccountSelected(accountId: UUID) {
        /*val fragment = AccountFragment.newInstance(accountId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()*/
        findNavController(this, R.id.nav_host_fragment).
        navigate(
            R.id.action_accountListFragment_to_accountFragment,
            AccountFragment.newBundle(accountId))
    }

    override fun onCurrencySelected(currencyId: UUID) {
        /*val fragment = CurrencyFragment.newInstance(currencyId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()*/
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.navigate(
            R.id.action_currencyListFragment_to_currencyFragment,
            CurrencyFragment.newBundle(currencyId))
    }

    override fun onCategorySelected(categoryId: UUID) {
        /*val fragment = CategoryFragment.newInstance(categoryId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()*/
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.navigate(
            R.id.action_categoryListFragment_to_categoryFragment,
            CategoryFragment.newBundle(categoryId))
    }

    override fun onTransactionSelected(transactionId: UUID) {
        /*val fragment = TransactionFragment.newInstance(transactionId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()*/
        findNavController(this, R.id.nav_host_fragment).
            navigate(
                R.id.action_transactionListFragment_to_transactionFragment,
                TransactionFragment.newBundle(transactionId))
    }

    /*private fun setCurrentFragment(fragment: Fragment) =
        /*supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
*/
}