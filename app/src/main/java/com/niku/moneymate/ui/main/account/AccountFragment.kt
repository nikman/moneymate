package com.niku.moneymate.ui.main.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountDetailViewModel
import com.niku.moneymate.R
import java.util.*
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.currency.CurrencyListViewModel
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.utils.SharedPrefs
import com.niku.moneymate.account.AccountExpenses
import com.niku.moneymate.utils.TransactionType

private const val ARG_ACCOUNT_ID = "account_id"
private const val TAG = "AccountFragment"

class AccountFragment: Fragment() {

    //private lateinit var viewModel: MainViewModel
    private lateinit var account: Account
    private lateinit var accountWithCurrency: AccountWithCurrency
    private lateinit var currency: MainCurrency
    private lateinit var currencies: List<MainCurrency>
    private lateinit var titleField: EditText
    private lateinit var noteField: EditText
    private lateinit var currencyField: Spinner
    private lateinit var balanceField: EditText
    private lateinit var initialBalanceField: EditText
    private lateinit var isDefaultAccountCheckBox: CheckBox
    private lateinit var accountChartField: BarChart

    private var initialAccountBalance: Double = 0.0
    private var accountBalance: Double = 0.0

    /*private val accountDetailViewModel: AccountDetailViewModel by lazy {
        ViewModelProvider(this)[AccountDetailViewModel::class.java]
    }*/

    private val accountDetailViewModel by activityViewModels<AccountDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        currency = MainCurrency(
            UUID.fromString(SharedPrefs().getStoredCurrencyId(requireContext())))
        account = Account(currency_id = currency.currency_id)
        accountWithCurrency = AccountWithCurrency(account, currency)
        val accountId: UUID = arguments?.getSerializable(ARG_ACCOUNT_ID) as UUID
        accountDetailViewModel.loadAccount(accountId)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.account_fragment, container, false)

        titleField = view.findViewById(R.id.account_title) as EditText
        noteField = view.findViewById(R.id.account_note) as EditText
        balanceField = view.findViewById(R.id.account_balance) as EditText
        initialBalanceField = view.findViewById(R.id.account_initial_balance) as EditText
        currencyField = view.findViewById(R.id.spinner) as Spinner
        isDefaultAccountCheckBox = view.findViewById(R.id.account_isDefault) as CheckBox
        accountChartField = view.findViewById(R.id.account_chart) as BarChart

        /*val viewModelFactory = CommonViewModelFactory()
        val currencyListViewModel: CurrencyListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[CurrencyListViewModel::class.java]
        }*/

        val currencyListViewModel by activityViewModels<CurrencyListViewModel>()

        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner,
            { currencies -> currencies?.let { updateCurrencyList(currencies) } }
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
                    this.accountWithCurrency = it
                    this.account = it.account
                    updateUI()
                }
            }
        )

        accountDetailViewModel.getAccountBalance(accountId).observe(
            viewLifecycleOwner,
            { account -> account?.let {
                this.accountBalance = it
                updateBalanceField()
            }
            }
        )

        accountDetailViewModel.getAccountExpensesData(accountId).observe(
            viewLifecycleOwner,
            { listOfExpenses -> listOfExpenses?.let {
                updateChartField(listOfExpenses)
            }
            }
        )

    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val changedText = s.toString()
                //Log.d(TAG, "Changed text: $changedText")
                account.title = changedText
            }

            override fun afterTextChanged(s: Editable?) {  }
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

        currencyField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                account.currency_id = currencies[position].currency_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        val balanceWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                account.initial_balance = if (count > 0) s.toString().toDouble() else 0.0
            }

            override fun afterTextChanged(s: Editable?) {  }
        }
        initialBalanceField.addTextChangedListener(balanceWatcher)

        isDefaultAccountCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                //currency.currency_is_default = isChecked
                if (isChecked) {
                    SharedPrefs().storeAccountId(context, account.account_id)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        accountDetailViewModel.saveAccount(account)
    }

    private fun updateUI() {

        titleField.setText(accountWithCurrency.account.title)
        noteField.setText(accountWithCurrency.account.note)
        initialBalanceField.setText(accountWithCurrency.account.initial_balance.toString())

        val uuidAsString = context?.applicationContext?.let {
            SharedPrefs().getStoredAccountId(it) }

        if (uuidAsString != null) {
            isDefaultAccountCheckBox.isChecked =
                uuidAsString.isNotEmpty() &&
                        account.account_id == UUID.fromString(uuidAsString)
        }

    }

    private fun updateBalanceField() {
        balanceField.setText(accountBalance.toString())
    }

    private fun updateChartField(listOfExpenses: List<AccountExpenses>) {

        val entriesOutcome: ArrayList<BarEntry> = ArrayList()
        val entriesIncome: ArrayList<BarEntry> = ArrayList()

        var i = 0.0f
        var j = 0.0f

        for (expenseItem in listOfExpenses) {
            if (expenseItem.type == TransactionType.OUTCOME) {
                entriesOutcome.add(
                    BarEntry(
                        //expenseItem.date?.toFloat() ?: 0.0f,
                        i++,
                        expenseItem.amount?.toFloat() ?: 0.0f
                    )
                )
            } else if (expenseItem.type == TransactionType.INCOME) {
                entriesIncome.add(
                    BarEntry(
                        //expenseItem.date?.toFloat() ?: 0.0f,
                        j++,
                        expenseItem.amount?.toFloat() ?: 0.0f
                    )
                )
            }
        }

        //val lineDataSet = LineDataSet(entries, "Label")
        val barData1 = BarDataSet(entriesOutcome, "Expenses")
        barData1.color = R.color.design_default_color_error
        val barData2 = BarDataSet(entriesIncome, "Incomes")
        barData2.color = R.color.design_default_color_primary

        //val data = LineData(lineDataSet)
        val data = BarData(barData1, barData2)
        //data.groupBars(0f, 1f, 3f)
        accountChartField.data = data

        accountChartField.invalidate()

    }

    private fun updateCurrencyList(currencies: List<MainCurrency>) {

        this.currencies = currencies

        val currenciesStrings = List(currencies.size)
            { i -> currencies[i].currency_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            currenciesStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencyField.adapter = adapter

        currencyField.setSelection(currencies.indexOf(accountWithCurrency.currency), true)

    }

    companion object {
        fun newBundle(account_id: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_ACCOUNT_ID, account_id) }
        }
    }

}