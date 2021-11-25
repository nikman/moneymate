package com.niku.moneymate.ui.main.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountDetailViewModel
import com.niku.moneymate.R
import com.niku.moneymate.currency.CurrencyDetailViewModel
import com.niku.moneymate.ui.main.common.MainViewModel
import java.util.*
import androidx.lifecycle.MediatorLiveData
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.currency.MainCurrency


private const val ARG_ACCOUNT_ID = "account_id"
//private const val ARG_CURRENCY_ID = "currency_id"
private const val TAG = "AccountFragment"

class AccountFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var account: Account
    private lateinit var accountWithCurrency: AccountWithCurrency
    private lateinit var currency: MainCurrency
    private lateinit var titleField: EditText
    private lateinit var noteField: EditText
    //private lateinit var currencyField: Spinner
    private lateinit var currencyField: EditText

    private val accountDetailViewModel: AccountDetailViewModel by lazy {
        ViewModelProvider(this)[AccountDetailViewModel::class.java]
    }

    private val currencyDetailViewModel: CurrencyDetailViewModel by lazy {
        ViewModelProvider(this)[CurrencyDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currency = MainCurrency(643, "RUB", UUID.fromString("3422b448-2460-4fd2-9183-8000de6f8343"))
        account = Account(currency.currency_id)
        accountWithCurrency = AccountWithCurrency(account, currency)
        val accountId: UUID = arguments?.getSerializable(ARG_ACCOUNT_ID) as UUID
        accountDetailViewModel.loadAccount(accountId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.account_fragment, container, false)

        titleField = view.findViewById(R.id.account_title) as EditText
        noteField = view.findViewById(R.id.account_note) as EditText
        //currencyField = view.findViewById(R.id.currency_spinner)
        currencyField = view.findViewById(R.id.account_currency) as EditText

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val accountId = arguments?.getSerializable(ARG_ACCOUNT_ID) as UUID
        //val currencyId = arguments?.getSerializable(ARG_CURRENCY_ID) as UUID

        //val mediatorLiveData = MediatorLiveData<Account>()

        accountDetailViewModel.loadAccount(accountId)

        val accountObserver = accountDetailViewModel.accountLiveData.observe(
            viewLifecycleOwner,
            {
                account -> account?.let {
                    this.accountWithCurrency = account
                    //currencyDetailViewModel.loadCurrency(account.currency_id)
                    updateUI()
            }
            }
        )
        /*val currencyObserver = currencyDetailViewModel.currencyLiveData.observe(
            viewLifecycleOwner,
            {
                    currency -> currency?.let {
                        this.currency = currency
                        //currencyDetailViewModel.loadCurrency(account.currency_id)
                        updateUI()
            }
            }
        )
        mediatorLiveData.addSource(accountDetailViewModel.accountLiveData, accountObserver)
        mediatorLiveData.addSource(currencyDetailViewModel.currencyLiveData, currencyObserver)
        mediatorLiveData.observe(accountObserver, currencyObserver)*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val changedText = s.toString()
                Log.d(TAG, "Changed text: $changedText")
                accountWithCurrency.account.title = changedText
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)

        val noteWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val changedText = s.toString()
                Log.d(TAG, "Changed text (note: $changedText")
                accountWithCurrency.account.note = changedText
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        noteField.addTextChangedListener(noteWatcher)

    }

    override fun onStop() {
        super.onStop()
        accountDetailViewModel.saveAccount(accountWithCurrency)
    }

    private fun updateUI() {

        titleField.setText(accountWithCurrency.account.title)
        noteField.setText(accountWithCurrency.account.note)
        //currencyField.setSelection(0)
        currencyField.setText(accountWithCurrency.currency.currency_title)

    }

    companion object {
        fun newInstance(account_id: UUID) : AccountFragment {
            val args = Bundle().apply {
                putSerializable(ARG_ACCOUNT_ID, account_id)
            }
            return AccountFragment().apply {
                arguments = args
            }
        }
    }

}