package com.niku.moneymate.ui.main.account

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.niku.moneymate.R
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountDetailViewModel
import com.niku.moneymate.account.AccountExpenses
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.currency.CurrencyListViewModel
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.ui.main.BaseFragmentEntity
import com.niku.moneymate.utils.TransactionType
import com.niku.moneymate.utils.getStoredAccountId
import com.niku.moneymate.utils.getStoredCurrencyId
import com.niku.moneymate.utils.storeAccountId
import java.util.*

private const val ARG_ACCOUNT_ID = "account_id"
private const val TAG = "AccountFragment"

class AccountFragment: Fragment(), BaseFragmentEntity {

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
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    //private var initialAccountBalance: Double = 0.0
    private var accountBalance: Double = 0.0

    private val accountDetailViewModel by activityViewModels<AccountDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        currency = MainCurrency(
            UUID.fromString(getStoredCurrencyId(requireContext())))
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
        saveButton = view.findViewById(R.id.ok_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val accountId = arguments?.getSerializable(ARG_ACCOUNT_ID) as UUID
        accountDetailViewModel.loadAccount(accountId)

        val currencyListViewModel by activityViewModels<CurrencyListViewModel>()

        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner
        ) { currencies -> currencies?.let { updateCurrencyList(currencies) } }

        accountDetailViewModel.accountLiveData.observe(
            viewLifecycleOwner
        ) { account ->
            account?.let {
                this.accountWithCurrency = it
                this.account = it.account
                updateUI()
            }
        }

        accountDetailViewModel.getAccountBalance(accountId).observe(
            viewLifecycleOwner
        ) { account ->
            account?.let {
                this.accountBalance = it
                updateBalanceField()
            }
        }

        accountDetailViewModel.getAccountExpensesData(accountId).observe(
            viewLifecycleOwner
        ) { listOfExpenses ->
            listOfExpenses?.let { updateChartField(listOfExpenses) }
        }
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
                view: View?,
                position: Int,
                id: Long
            ) {
                currencies.let {
                    account.currency_id = currencies[position].currency_id
                }

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
                    storeAccountId(context, account.account_id)
                }
            }
        }

        saveButton.apply {
            setOnClickListener {
                SaveEntiy(account)
            }
        }

        cancelButton.apply {
            setOnClickListener {
                CloseWithoutSaving()
            }
        }

    }

    private fun updateUI() {

        titleField.setText(accountWithCurrency.account.title)
        noteField.setText(accountWithCurrency.account.note)
        //initialBalanceField.setText(accountWithCurrency.account.initial_balance.toString())

        val uuidAsString =
            getStoredAccountId(requireContext())

        if (uuidAsString != null) {
            isDefaultAccountCheckBox.isChecked =
                uuidAsString.isNotEmpty() &&
                        account.account_id == UUID.fromString(uuidAsString)
        }

    }

    private fun updateBalanceField() {
        balanceField.setText(accountBalance.toString())
    }

    private inner class DayAxisValueFormatter(private val labelsIncome: MutableMap<*, *>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return labelsIncome[value].toString()
        }
    }

    private fun updateChartField(listOfExpenses: List<AccountExpenses>) {

        val entriesOutcome: ArrayList<BarEntry> = ArrayList()
        val entriesIncome: ArrayList<BarEntry> = ArrayList()

        var i = 0.0f

        val labelsIncome =
            mutableMapOf(
                -1f to "earlier",
                0f to "1",
                1f to "2",
                2f to "3",
                3f to "4",
                4f to "5",
                5f to "6",
                6f to "older",
            )

        for (expenseItem in listOfExpenses) {
            if (expenseItem.type == TransactionType.OUTCOME) {
                labelsIncome[i] = expenseItem.date.toString()
                entriesOutcome.add(
                    BarEntry(
                        i++,
                        0f - (expenseItem.amount?.toFloat() ?: 0.0f)
                    )
                )
            }
        }

        i = 0f

        for (expenseItem in listOfExpenses) {
            if (expenseItem.type == TransactionType.INCOME) {
                entriesIncome.add(
                    BarEntry(
                        i++,
                        expenseItem.amount?.toFloat() ?: 0.0f
                    )
                )
            }
        }

        //val lineDataSet = LineDataSet(entries, "Label")
        val barData1 = BarDataSet(entriesOutcome, "Expenses")
        barData1.color = R.color.red_400
        val barData2 = BarDataSet(entriesIncome, "Incomes")
        barData2.color = R.color.green_400

        val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(labelsIncome)

        //val groupSpace = 0.08f
        //val barSpace = 0.03f // x4 DataSet

        //val barWidth = 0.2f // x4 DataSet

        val data = BarData(barData1, barData2)
        //data.barWidth = 0.9f
        //data.groupBars(0f, groupSpace, barSpace)
        accountChartField.data = data
        accountChartField.setFitBars(true)
        val xAxis = accountChartField.xAxis
        //xAxis.setCenterAxisLabels(true)

        xAxis.valueFormatter = xAxisFormatter

        //accountChartField.invalidate()
        accountChartField.animateY(600)

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

    override fun CloseWithoutSaving() {
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        findNavController().popBackStack()
    }

    override fun SaveEntiy(entity: Any) {
        accountDetailViewModel.saveAccount(account = this.account)
        findNavController().popBackStack()
    }

    companion object {
        fun newBundle(account_id: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_ACCOUNT_ID, account_id) }
        }
    }

}