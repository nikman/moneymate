package com.niku.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.niku.moneymate.ui.main.account.AccountFragment
import com.niku.moneymate.ui.main.account.AccountListFragment
import com.niku.moneymate.ui.main.category.CategoryListFragment
import java.util.*

class MainActivity : AppCompatActivity(), AccountListFragment.Callbacks {

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

    private fun setCurrentFragment(fragment: Fragment) =
        /*supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()

}