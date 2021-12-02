package com.niku.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        if (savedInstanceState == null) {
            setCurrentFragment(AccountListFragment.newInstance())
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener  {

            when(it.itemId) {

                R.id.budget->setCurrentFragment(AccountListFragment.newInstance())
                R.id.accounts->setCurrentFragment(AccountListFragment.newInstance())
                R.id.categories->setCurrentFragment(CategoryListFragment.newInstance())
                R.id.projects->setCurrentFragment(CategoryListFragment.newInstance())
                R.id.currencies->setCurrentFragment(CurrencyListFragment.newInstance())

            }
            true
        }

    }

    override fun onAccountSelected(accountId: UUID) {
        val fragment = AccountFragment.newInstance(accountId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCurrencySelected(currencyId: UUID) {
        val fragment = CurrencyFragment.newInstance(currencyId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCategorySelected(categoryId: UUID) {
        val fragment = CategoryFragment.newInstance(categoryId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onTransactionSelected(transactionId: UUID) {
        val fragment = TransactionFragment.newInstance(transactionId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        /*supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()

}