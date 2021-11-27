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
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountDetailViewModel
import com.niku.moneymate.R
import com.niku.moneymate.ui.main.common.MainViewModel
import java.util.*
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.currency.CurrencyListViewModel
import com.niku.moneymate.currency.CurrencyViewModelFactory
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.database.MoneyMateRepository
import androidx.lifecycle.Observer
import com.niku.moneymate.utils.SharedPrefs


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
    private lateinit var currencyField: Spinner
    //private lateinit var currencyField: EditText

    private val accountDetailViewModel: AccountDetailViewModel by lazy {
        ViewModelProvider(this)[AccountDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //currency = MainCurrency(643, "RUB", UUID.fromString("0f967f94-dca8-4e2a-8019-850b0dd9ea38"))
        currency = MainCurrency(
            UUID.fromString(context?.applicationContext?.let { SharedPrefs().getStoredCurrencyId(it) }))
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
        currencyField = view.findViewById(R.id.spinner) as Spinner

        val viewModelFactory = CurrencyViewModelFactory()
        val currencyListViewModel: CurrencyListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[CurrencyListViewModel::class.java]
        }
        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner,
            Observer { currencies -> currencies?.let { updateCurrencyList(currencies) } }
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val accountId = arguments?.getSerializable(ARG_ACCOUNT_ID) as UUID
        accountDetailViewModel.loadAccount(accountId)

        accountDetailViewModel.accountLiveData.observe(
            viewLifecycleOwner,
            {
                account -> account?.let {
                    this.accountWithCurrency = account
                    this.account = account.account
                    updateUI()
            }
            }
        )

        /*currencyDetailViewModel.currencyLiveData.observe(
            viewLifecycleOwner,
            {
                    currency -> currency?.let {
                this.accountWithCurrency = currency
                this.account = account.account
                updateUI()
            }
            }
        )*/

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
                account.title = changedText
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
                account.note = changedText
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        noteField.addTextChangedListener(noteWatcher)

    }

    override fun onStop() {
        super.onStop()
        accountDetailViewModel.saveAccount(account)
    }

    private fun updateUI() {

        titleField.setText(accountWithCurrency.account.title)
        noteField.setText(accountWithCurrency.account.note)
        //currencyField.setSelection(0)
        //currencyField.setText(accountWithCurrency.currency.currency_title)

    }

    private fun updateCurrencyList(currencies: List<MainCurrency>) {

        val currenciesStrings = List<String>(currencies.size)
            { i -> currencies[i].currency_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            currenciesStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyField.adapter = adapter
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