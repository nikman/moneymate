package com.niku.moneymate.ui.main.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.niku.moneymate.CommonViewModelFactory
import com.niku.moneymate.R
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountListViewModel
import com.niku.moneymate.category.Category
import com.niku.moneymate.category.CategoryListViewModel
import com.niku.moneymate.currency.CurrencyListViewModel
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project
import com.niku.moneymate.projects.ProjectListViewModel
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionDetailViewModel
import com.niku.moneymate.transaction.TransactionWithProperties
import com.niku.moneymate.ui.main.common.MainViewModel
import com.niku.moneymate.utils.CategoryType
import com.niku.moneymate.utils.SharedPrefs
import com.niku.moneymate.utils.TransactionType
import com.niku.moneymate.utils.UUID_ACCOUNT_EMPTY
import java.util.*

private const val ARG_TRANSACTION_ID = "transaction_id"
private const val TAG = "TransactionFragment"

class TransactionFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var transactionWithProperties: TransactionWithProperties

    //private var multiplier: Byte = 0

    private lateinit var moneyTransaction: MoneyTransaction
    private lateinit var currency: MainCurrency
    private lateinit var accountFrom: Account
    private lateinit var accountTo: Account
    private lateinit var category: Category
    private lateinit var project: Project

    private lateinit var currencies: List<MainCurrency>
    private lateinit var accounts: List<Account>
    private lateinit var categories: List<Category>
    private lateinit var projects: List<Project>

    private lateinit var dateButton: Button
    private lateinit var accountFromField: Spinner
    private lateinit var accountToField: Spinner
    private lateinit var currencyField: Spinner
    private lateinit var categoryField: Spinner
    private lateinit var amountField: EditText
    private lateinit var projectField: Spinner
    private lateinit var transactionTypeImageButton: ImageButton

    private val moneyTransactionDetailViewModel: TransactionDetailViewModel by lazy {
        ViewModelProvider(this)[TransactionDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        currency = MainCurrency(
            UUID.fromString(SharedPrefs().getStoredCurrencyId(requireContext())))

        accountFrom = Account(
            currency_id = currency.currency_id,
            account_id = UUID.fromString(SharedPrefs().getStoredAccountId(requireContext())))

        accountTo = Account(
            currency_id = currency.currency_id,
            account_id = UUID.fromString(SharedPrefs().getStoredAccountId(requireContext())))

        category = Category(
            category_id = UUID.fromString(SharedPrefs().getStoredCategoryId(requireContext())))

        project = Project(
            project_id = UUID.fromString(SharedPrefs().getStoredProjectId(requireContext())))

        moneyTransaction = MoneyTransaction(
            accountFrom.account_id,
            //accountTo.account_id,
            UUID.fromString(UUID_ACCOUNT_EMPTY),
            currency.currency_id,
            category.category_id,
            project.project_id)

        transactionWithProperties =
            TransactionWithProperties(
                moneyTransaction,
                accountFrom,
                accountTo,
                currency,
                category,
                project)

        val moneyTransactionId: UUID = arguments?.getSerializable(ARG_TRANSACTION_ID) as UUID
        moneyTransactionDetailViewModel.loadTransaction(moneyTransactionId)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.money_transaction_fragment, container, false)

        dateButton = view.findViewById(R.id.transaction_date) as Button
        accountFromField = view.findViewById(R.id.account_from_spinner) as Spinner
        accountToField = view.findViewById(R.id.account_to_spinner) as Spinner
        currencyField = view.findViewById(R.id.currency_spinner) as Spinner
        amountField = view.findViewById(R.id.transaction_amount) as EditText
        categoryField = view.findViewById(R.id.category_spinner) as Spinner
        projectField = view.findViewById(R.id.project_spinner) as Spinner
        transactionTypeImageButton = view.findViewById(R.id.image_button_transaction_type)

        val viewModelFactory = CommonViewModelFactory()

        val accountListViewModel: AccountListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[AccountListViewModel::class.java]
        }
        accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner,
            Observer { accounts -> accounts?.let { updateAccountsList(accounts) } }
        )

        val currencyListViewModel: CurrencyListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[CurrencyListViewModel::class.java]
        }
        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner,
            Observer { currencies -> currencies?.let { updateCurrenciesList(currencies) } }
        )

        val categoryListViewModel: CategoryListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[CategoryListViewModel::class.java]
        }
        categoryListViewModel.categoryListLiveData.observe(
            viewLifecycleOwner,
            Observer { categories -> categories?.let { updateCategoriesList(categories) } }
        )

        val projectListViewModel: ProjectListViewModel by lazy {
            ViewModelProvider(viewModelStore, viewModelFactory)[ProjectListViewModel::class.java]
        }
        projectListViewModel.projectListLiveData.observe(
            viewLifecycleOwner,
            Observer { projects -> projects?.let { updateProjectsList(projects) } }
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val transactionId = arguments?.getSerializable(ARG_TRANSACTION_ID) as UUID
        moneyTransactionDetailViewModel.loadTransaction(transactionId)

        moneyTransactionDetailViewModel.transactionLiveData.observe(
            viewLifecycleOwner,
            {
                transaction -> transaction?.let {
                    this.transactionWithProperties = transaction
                    this.moneyTransaction = transactionWithProperties.transaction
                    this.accountFrom = transactionWithProperties.accountFrom
                    this.accountTo = transactionWithProperties.accountTo
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

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dateButton.setOnClickListener {

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener {
                    _, year, monthOfYear, dayOfMonth ->
                val selectedDate = GregorianCalendar(year, monthOfYear, dayOfMonth).time
                dateButton.text = selectedDate.toString()
                moneyTransaction.transactionDate = selectedDate
            }, year, month, day)
            dpd.show()
        }

        val amountWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                moneyTransaction.amount_from = if (count > 0) s.toString().toDouble() else 0.0
                moneyTransaction.amount_to = moneyTransaction.amount_from
            }

            override fun afterTextChanged(s: Editable?) {  }
        }
        amountField.addTextChangedListener(amountWatcher)

        accountFromField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                moneyTransaction.account_id_from = accounts[position].account_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        accountToField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //moneyTransaction.account_id_to = accounts[position].account_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        currencyField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                moneyTransaction.currency_id = currencies[position].currency_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                moneyTransaction.category_id = categories[position].category_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        transactionTypeImageButton.apply {
            setOnClickListener { _, ->
                setImageButton(revert = true)
            }
        }

    }

    private fun setImageButton(revert: Boolean = false) {

        if (revert) {
            when (transactionWithProperties.transaction.transaction_type) {
                TransactionType.INCOME -> {
                    transactionTypeImageButton.setImageResource(R.drawable.ic_outcom_36)
                    transactionWithProperties.transaction.transaction_type = TransactionType.OUTCOME
                }
                TransactionType.OUTCOME -> {
                    transactionTypeImageButton.setImageResource(R.drawable.ic_income_36)
                    transactionWithProperties.transaction.transaction_type = TransactionType.INCOME
                }
            }
        } else
            when (transactionWithProperties.transaction.transaction_type) {
                        TransactionType.OUTCOME -> transactionTypeImageButton.setImageResource(R.drawable.ic_outcom_36)
                        TransactionType.INCOME -> transactionTypeImageButton.setImageResource(R.drawable.ic_income_36)
                        else -> if (transactionWithProperties.category.category_type == CategoryType.INCOME) {
                            transactionTypeImageButton.setImageResource(R.drawable.ic_income_36)
                            transactionWithProperties.transaction.transaction_type = TransactionType.INCOME
                        } else {
                            transactionTypeImageButton.setImageResource(R.drawable.ic_outcom_36)
                            transactionWithProperties.transaction.transaction_type = TransactionType.OUTCOME
                        }
            }
    }

    override fun onStop() {
        super.onStop()
        moneyTransactionDetailViewModel.saveTransaction(moneyTransaction)

    }

    private fun updateUI() {

        amountField.setText(moneyTransaction.amount_from.toString())
        dateButton.text = moneyTransaction.transactionDate.toString()

        if (this::accounts.isInitialized) {
            accountToField.setSelection(accounts.indexOf(accountTo), true)
            accountFromField.setSelection(accounts.indexOf(accountFrom), true)
        }
        if (this::currencies.isInitialized) {
            currencyField.setSelection(currencies.indexOf(currency), true)
        }
        if (this::categories.isInitialized) {
            categoryField.setSelection(categories.indexOf(category), true)
        }

        setImageButton(revert = false)
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

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        accountFromField.adapter = adapter
        accountToField.adapter = adapter
    }

    private fun updateCurrenciesList(currencies: List<MainCurrency>) {

        this.currencies = currencies

        val currenciesStrings = List<String>(currencies.size)
        { i -> currencies[i].currency_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            currenciesStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyField.adapter = adapter
    }

    private fun updateCategoriesList(categories: List<Category>) {

        this.categories = categories

        val categoriesStrings = List<String>(categories.size)
        { i -> categories[i].category_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categoriesStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryField.adapter = adapter
    }

    private fun updateProjectsList(projects: List<Project>) {

        this.projects = projects

        val projectsStrings = List<String>(projects.size)
        { i -> projects[i].project_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            projectsStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectField.adapter = adapter
    }

    companion object {

        fun  newBundle(transaction_id: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_TRANSACTION_ID, transaction_id) }
        }
    }

}