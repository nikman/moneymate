package com.niku.moneymate.ui.main.transaction

import android.app.DatePickerDialog
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.fragment.findNavController
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
import com.niku.moneymate.ui.main.BaseFragmentEntity
import com.niku.moneymate.utils.CategoryType
import com.niku.moneymate.utils.SharedPrefs
import com.niku.moneymate.utils.TransactionType
import com.niku.moneymate.utils.UUID_ACCOUNT_EMPTY
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

private const val ARG_TRANSACTION_ID = "transaction_id"
private const val TAG = "TransactionFragment"

class TransactionFragment : Fragment(), BaseFragmentEntity {

    private lateinit var transactionWithProperties: TransactionWithProperties

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
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private val moneyTransactionDetailViewModel by activityViewModels<TransactionDetailViewModel>()

    //fun <T, A, B> LiveData<A>.combineAndCompute(other: List<LiveData<B>>, onChange: (A, B) -> T): MediatorLiveData<T> {
    fun <T, A, B> LiveData<A>.combineAndCompute(other: List<LiveData<B>>, onChange: (A, ArrayList<B?>) -> T): MediatorLiveData<T> {

        var source1emitted = false
        /*var source2emitted = false*/
        val listOfSourcesEmitted = ArrayList<Boolean>()

        val result = MediatorLiveData<T>()

        val mergeF = {
            val source1Value = this.value
            //val source2Value = other.value
            val listOfOtherSourcesValues = ArrayList<B?>()

            other.forEach { listOfOtherSourcesValues.add(it.value) }

            //if (source1emitted && source2emitted) {
            if (source1emitted && listOfSourcesEmitted.all { it }) {
                result.value = onChange.invoke(source1Value!!, listOfOtherSourcesValues )
            }
        }

        result.addSource(this) { source1emitted = true; mergeF.invoke() }
        other.forEachIndexed { index, liveData ->
            result.addSource(liveData) { listOfSourcesEmitted[index] = true; mergeF.invoke() }
        }

        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "onCreate")

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

        Log.d(TAG, "onCreateView")

        val view = inflater.inflate(R.layout.money_transaction_fragment, container, false)

        dateButton = view.findViewById(R.id.transaction_date) as Button
        dateButton.visibility = View.GONE
        accountFromField = view.findViewById(R.id.account_from_spinner) as Spinner
        accountToField = view.findViewById(R.id.account_to_spinner) as Spinner
        currencyField = view.findViewById(R.id.currency_spinner) as Spinner
        amountField = view.findViewById(R.id.transaction_amount) as EditText
        categoryField = view.findViewById(R.id.category_spinner) as Spinner
        projectField = view.findViewById(R.id.project_spinner) as Spinner
        transactionTypeImageButton = view.findViewById(R.id.image_button_transaction_type)
        saveButton = view.findViewById(R.id.ok_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.d(TAG, "onViewCreated")

        super.onViewCreated(view, savedInstanceState)

        val transactionId = arguments?.getSerializable(ARG_TRANSACTION_ID) as UUID
        moneyTransactionDetailViewModel.loadTransaction(transactionId)

        val transactionLiveData = moneyTransactionDetailViewModel.transactionLiveData
        /*.observe(
            viewLifecycleOwner
        ) { transaction ->
            transaction?.let {
                this.transactionWithProperties = transaction
                this.moneyTransaction = transactionWithProperties.transaction
                this.accountFrom = transactionWithProperties.accountFrom
                this.accountTo = transactionWithProperties.accountTo
                this.currency = transactionWithProperties.currency
                this.category = transactionWithProperties.category
                updateUI()
            }
        }*/

        val accountListViewModel by activityViewModels<AccountListViewModel>()
        val accountListLiveData = accountListViewModel.accountListLiveData

        //transactionLiveData.combineAndCompute(accountListLiveData) { _, _ -> {  } }.observe(viewLifecycleOwner) { updateUI() }
        /*accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner,
            Observer { accounts -> accounts?.let { updateAccountsList(accounts) } }
        )*/

        val currencyListViewModel by activityViewModels<CurrencyListViewModel>()
        val currencyListLiveData = currencyListViewModel.currencyListLiveData
        /*currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner,
            Observer { currencies -> currencies?.let { updateCurrenciesList(currencies) } }
        )*/

        val categoryListViewModel by activityViewModels<CategoryListViewModel>()
        val categoryListLiveData = categoryListViewModel.categoryListLiveData

        /*categoryListViewModel.categoryListLiveData.observe(
            viewLifecycleOwner,
            Observer { categories -> categories?.let { updateCategoriesList(categories) } }
        )*/

        val projectListViewModel by activityViewModels<ProjectListViewModel>()
        val projectListLiveData = projectListViewModel.projectListLiveData

        /*projectListViewModel.projectListLiveData.observe(
            viewLifecycleOwner,
            Observer { projects -> projects?.let { updateProjectsList(projects) } }
        )*/

        //val result = MediatorLiveData<Int>()

        /*result.observe(viewLifecycleOwner) {
            updateUI()
        }*/

        /*result.addSource(transactionLiveData) { transaction ->
            transaction?.let {
                this.transactionWithProperties = transaction
                this.moneyTransaction = transactionWithProperties.transaction
                this.accountFrom = transactionWithProperties.accountFrom
                this.accountTo = transactionWithProperties.accountTo
                this.currency = transactionWithProperties.currency
                this.category = transactionWithProperties.category
                this.project = transactionWithProperties.project
                //updateUI()
                Log.d(TAG, "transaction got")
            }
        }
        result.addSource(accountListLiveData) {
                accounts -> accounts?.let {
                    this.accounts = accounts
                    Log.d(TAG, "accounts got")
                    //updateUI()
                }
        }
        result.addSource(currencyListLiveData) { currencies ->
            currencies?.let {
                this.currencies = currencies
                Log.d(TAG, "currencies got")
                //updateUI()
            }
        }

        result.addSource(categoryListLiveData) { categories ->
            categories?.let {
                this.categories = categories
                Log.d(TAG, "categories got")
                //updateUI()
            }
        }
        result.addSource(projectListLiveData) { projects ->
            projects?.let {
                this.projects = projects
                Log.d(TAG, "projects got")
                //updateUI()
            }
        }*/

        /*val dataForCompute = listOf(
            accountListLiveData,
            currencyListLiveData,
            categoryListLiveData,
            projectListLiveData)

        val lambdas = listOf(
            { accounts: List<Account> -> accounts?.let {
                this.accounts = accounts
                Log.d(TAG, "accounts got")
                //updateUI()
            }
            },
            { currencies: List<MainCurrency> ->
                currencies?.let {
                    this.currencies = currencies
                    Log.d(TAG, "currencies got")
                    //updateUI()
                }
        )

        transactionLiveData.combineAndCompute(dataForCompute, lambdas)*/

    }

    override fun onStart() {

        //Log.d(TAG, "onStart")

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
                moneyTransaction.amount_from = if (count > 0) s.toString().toDouble() * moneyTransaction.transaction_type else 0.0
                moneyTransaction.amount_to = 0.0 //-moneyTransaction.amount_from
            }

            override fun afterTextChanged(s: Editable?) {  }
        }
        amountField.addTextChangedListener(amountWatcher)

        transactionTypeImageButton.apply {
            setOnClickListener { _, ->
                setImageButton(revert = true)
            }
        }

        saveButton.apply {
            setOnClickListener {
                SaveEntiy(moneyTransaction)
            }
        }

        cancelButton.apply {
            setOnClickListener {
                CloseWithoutSaving()
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

    private fun updateUI() {

        Log.d(TAG, "updateUI_11")

        if (moneyTransaction != null
            && accounts != null
            && currencies != null
            && categories != null
            && projects != null) {

            Log.d(TAG, "updateUI_2")

            amountField.setText(abs(moneyTransaction.amount_from).toString())
            dateButton.text = moneyTransaction.transactionDate.toString()

            updateAccountsList(accounts)
            updateCurrenciesList(currencies)
            updateCategoriesList(categories)
            updateProjectsList(projects)

            accountToField.setSelection(accounts.indexOf(accountTo), false)
            accountFromField.setSelection(accounts.indexOf(accountFrom), false)
            currencyField.setSelection(currencies.indexOf(currency), false)
            categoryField.setSelection(categories.indexOf(category), false)
            projectField.setSelection(projects.indexOf(project), false)

            setImageButton(revert = false)

        }

    }

    private fun updateAccountsList(accounts: List<Account>) {

       //this.accounts = accounts

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

        Log.d(TAG, "updateAccountsList")

        accountFromField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "updateAccountsList - onItemSelected")
                moneyTransaction.account_id_from = accounts[position].account_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        accountToField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long) {
                moneyTransaction.account_id_to = accounts[position].account_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        projectField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "updateAccountsList - projectField - onItemSelected")
                moneyTransaction.project_id = projects[position].project_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

    }

    private fun updateCurrenciesList(currencies: List<MainCurrency>) {

        Log.d(TAG, "updateCurrenciesList")

        //this.currencies = currencies

        val currenciesStrings = List<String>(currencies.size)
        { i -> currencies[i].currency_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            currenciesStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyField.adapter = adapter

        currencyField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "updateCurrenciesList - onItemSelected")
                moneyTransaction.currency_id = currencies[position].currency_id
                //moneyTransaction.currency_id = UUID.fromString(parent.getItemAtPosition(position) as String)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }


    }

    private fun updateCategoriesList(categories: List<Category>) {

        //this.categories = categories
        Log.d(TAG, "updateCategoriesList")

        val categoriesStrings = List<String>(categories.size)
        { i -> categories[i].category_title }

        val adapter: ArrayAdapter<*>

        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categoriesStrings)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryField.adapter = adapter

        categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                moneyTransaction.category_id = categories[position].category_id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

    }

    private fun updateProjectsList(projects: List<Project>) {

        //this.projects = projects
        Log.d(TAG, "updateProjectsList")

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

    override fun CloseWithoutSaving() {
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        findNavController().popBackStack()
    }

    override fun SaveEntiy(entity: Any) {
        moneyTransactionDetailViewModel.saveTransaction(transaction = this.moneyTransaction)
        findNavController().popBackStack()
    }

    companion object {

        fun  newBundle(transaction_id: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_TRANSACTION_ID, transaction_id) }
        }
    }

}