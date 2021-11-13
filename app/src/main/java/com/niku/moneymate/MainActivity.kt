package com.niku.moneymate

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.niku.moneymate.ui.main.AccountFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AccountFragment.newInstance())
                    .commitNow()
        }
    }
}