package com.niku.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.niku.moneymate.ui.main.AccountFragment
import com.niku.moneymate.ui.main.AccountListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountListFragment.newInstance())
                .commitNow()
        }
    }
}