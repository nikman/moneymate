package com.niku.moneymate.ui.main.transaction

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.R
import com.niku.moneymate.ui.main.common.MainViewModel
import java.util.*
import com.niku.moneymate.currency.CurrencyListViewModel
import com.niku.moneymate.currency.MainCurrency
import androidx.lifecycle.Observer
import com.niku.moneymate.CommonViewModelFactory
import com.niku.moneymate.account.AccountListViewModel
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionDetailViewModel
import com.niku.moneymate.transaction.TransactionListViewModel
import com.niku.moneymate.transaction.TransactionWithProperties
import com.niku.moneymate.utils.SharedPrefs


private const val ARG_TRANSACTION_ID = "transaction_id"
//private const val ARG_CURRENCY_ID = "currency_id"
private const val TAG = "TransactionFragment"

class TransactionFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var moneyTransaction: MoneyTransaction
    private lateinit var transactionWithProperties: TransactionWithProperties
    private lateinit var currency: MainCurrency
    private lateinit var account: Account
    private lateinit var currencies: List<MainCurrency>
    private lateinit var accounts: List<Account>
    private lateinit var category: Category

    private lateinit var dateButton: Button
    private lateinit var accountField: Spinner
    private lateinit var amountField: EditText
    private lateinit var currencyField: Spinner

    private val moneyTransactionDetailViewModel: TransactionDetailViewModel by lazy {
        ViewModelProvider(this)[TransactionDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //currency = MainCurrency(643, "RUB", UUID.fromString("0f967f94-dca8-4e2a-8019-850b0dd9ea38"))
        currency = MainCurrency(
            UUID.fromString(context?.applicationContext?.let { SharedPrefs().getStoredCurrencyId(it) }))
        account = Account(currency.currency_id)
        category = Category()
        moneyTransaction = MoneyTransaction(account.account_id, currency.currency_id, category.category_id)
        transactionWithProperties = TransactionWithProperties(moneyTransaction, account, currency, category)

        val moneyTransactionId: UUID = arguments?.getSerializable(ARG_TRANSACTION_ID) as UUID
        moneyTransactionDetailViewModel.loadTransaction(moneyTransactionId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.money_transaction_fragment, container, false)

        dateButton = view.findViewById(R.id.transaction_date) as Button
        accountField = view.findViewById(R.id.account_spinner) as Spinner
        amountField = view.findViewById(R.id.transaction_amount) as EditText

        val viewModelFactory = CommonViewModelFactory()
        /*val transactionListViewModel: TransactionListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[TransactionListViewModel::class.java]
        }*/
        val accountListViewModel: AccountListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[AccountListViewModel::class.java]
        }
        accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner,
            Observer { accounts -> accounts?.let { updateAccountsList(accounts) } }
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val transactiontId = arguments?.getSerializable(ARG_TRANSACTION_ID) as UUID
        moneyTransactionDetailViewModel.loadTransaction(transactiontId)

        moneyTransactionDetailViewModel.transactionLiveData.observe(
            viewLifecycleOwner,
            {
                    transaction -> transaction?.let {
                this.transactionWithProperties = transaction
                this.account = transactionWithProperties.account
                this.currency = transactionWithProperties.currency
                this.category = transactionWithProperties.category
                updateUI()
            }
            }
        )

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        accountField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                moneyTransaction.account_id = accounts[position].account_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }


    }

    override fun onStop() {
        super.onStop()
        moneyTransactionDetailViewModel.saveTransaction(moneyTransaction)
    }

    private fun updateUI() {

        //titleField.setText(accountWithCurrency.account.title)
        amountField.setText(transactionWithProperties.transaction.amount.toString())
        accountField.setSelection(accounts.indexOf(transactionWithProperties.account), true)
        //currencyField.setText(accountWithCurrency.currency.currency_title)

    }

    private fun updateAccountsList(accounts: List<Account>) {

        this.accounts = accounts

        val accountsStrings = List<String>(accounts.size)
        { i -> accounts[i].title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            accountsStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountField.adapter = adapter
    }

    companion object {
        fun newInstance(transaction_id: UUID) : TransactionFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TRANSACTION_ID, transaction_id)
            }
            return TransactionFragment().apply {
                arguments = args
            }
        }
    }

}