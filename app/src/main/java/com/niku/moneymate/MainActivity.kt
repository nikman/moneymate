package com.niku.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.niku.moneymate.ui.main.account.AccountFragment
import com.niku.moneymate.ui.main.account.AccountListFragment
import java.util.*

class MainActivity : AppCompatActivity(), AccountListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountListFragment.newInstance())
                .commitNow()
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

}