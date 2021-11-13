package com.niku.moneymate.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.niku.moneymate.AccountListViewModel
import com.niku.moneymate.AccountViewModelFactory
import kotlin.reflect.KProperty

private const val TAG = "AccountListFragment"

class AccountListFragment: Fragment() {

    /*private val accountListViewModel: AccountListViewModel by lazy {
        ViewModelProvider(this).get(AccountListViewModel::class.java)
    }*/

    private val viewModelFactory = AccountViewModelFactory()

    private val accountListViewModel =
        ViewModelProvider(viewModelStore, viewModelFactory).get(AccountListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total accounts: ${accountListViewModel.accounts.size}")
    }

    companion object {
        fun newInstance(): AccountListFragment {return AccountListFragment()}
    }
}


