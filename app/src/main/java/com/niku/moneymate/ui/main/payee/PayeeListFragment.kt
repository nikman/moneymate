package com.niku.moneymate.ui.main.payee

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.CommonViewModelFactory
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountListViewModel
import com.niku.moneymate.R
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.payee.PayeeListViewModel
import com.niku.moneymate.utils.SharedPrefs
import java.util.*

private const val TAG = "PayeeListFragment"

class PayeeListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        //setContent {

        //}
    }

    @Composable
    fun MyExample(payeeListViewModel: PayeeListViewModel) {
        val dataExample = payeeListViewModel.payeeListLiveData.observeAsState()
        dataExample.value?.let {
            //ShowData(dataExample)
            Text(dataExample.value.toString())
        }
    }

    companion object {
        fun newInstance(): PayeeListFragment {return PayeeListFragment()}
    }
}


