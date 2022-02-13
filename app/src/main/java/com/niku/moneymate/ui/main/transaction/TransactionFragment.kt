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
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.niku.moneymate.utils.*
import java.util.*
import kotlin.math.abs

private const val ARG_TRANSACTION_ID = "transaction_id"
private const val TAG = "TransactionFragment"

class TransactionFragment : Fragment(), BaseFragmentEntity {

    private var transactionGot = false
    private var accountsGot = false
    private var projectsGot = false
    private var categoriesGot = false
    private var currenciesGot = false

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
    private lateinit var transactionTypeImageButton: ImageButton
    private lateinit var amountField: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var listPopupAccountFromButton: Button
    private lateinit var listPopupWindowAccountFrom: ListPopupWindow
    private lateinit var listPopupAccountToButton: Button
    private lateinit var listPopupWindowAccountTo: ListPopupWindow
    private lateinit var listPopupCategoryButton: Button
    private lateinit var listPopupWindowCategory: ListPopupWindow
    private lateinit var listPopupProjectButton: Button
    private lateinit var listPopupWindowProject: ListPopupWindow
    private lateinit var listPopupCurrencyButton: Button
    private lateinit var listPopupWindowCurrency: ListPopupWindow

    private lateinit var arrowForwardImage: ImageView

    private val moneyTransactionDetailViewModel by activityViewModels<TransactionDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "onCreate")

        super.onCreate(savedInstanceState)

        currency = MainCurrency(
            UUID.fromString(SharedPrefs().getStoredCurrencyId(requireContext())))


        accountFrom = Account(
            currency_id = currency.currency_id,
            account_id = UUID.fromString(SharedPrefs().getStoredAccountId(requireContext())))

        accountTo = accountFrom.copy()

        category = Category(
            category_id = UUID.fromString(SharedPrefs().getStoredCategoryId(requireContext())))

        project = Project(
            project_id = UUID.fromString(SharedPrefs().getStoredProjectId(requireContext())))

        moneyTransaction = MoneyTransaction(
            account_id_from = accountFrom.account_id,//UUID.fromString(UUID_ACCOUNT_EMPTY),
            account_id_to = UUID.fromString(UUID_ACCOUNT_EMPTY),
            currency_id = currency.currency_id,
            category_id = UUID.fromString(UUID_CATEGORY_EMPTY),//category.category_id,
            project_id = UUID.fromString(UUID_PROJECT_EMPTY))//project.project_id)

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
        amountField = view.findViewById(R.id.transaction_amount) as EditText
        transactionTypeImageButton = view.findViewById(R.id.image_button_transaction_type)
        saveButton = view.findViewById(R.id.ok_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        val maxWidth = requireContext().resources.displayMetrics.widthPixels - 20

        listPopupAccountFromButton = view.findViewById(R.id.button_account_from_v2)
        listPopupWindowAccountFrom =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowAccountFrom.anchorView = listPopupAccountFromButton
        listPopupWindowAccountFrom.width = maxWidth

        listPopupAccountToButton = view.findViewById(R.id.button_account_to_v2)
        listPopupWindowAccountTo =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowAccountTo.anchorView = listPopupAccountToButton
        listPopupWindowAccountTo.width = maxWidth

        listPopupCategoryButton = view.findViewById(R.id.button_category_v2)
        listPopupWindowCategory =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowCategory.anchorView = listPopupCategoryButton
        listPopupWindowCategory.width = maxWidth

        listPopupProjectButton = view.findViewById(R.id.button_project_v2)
        listPopupWindowProject =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowProject.anchorView = listPopupProjectButton
        listPopupWindowProject.width = maxWidth

        listPopupCurrencyButton = view.findViewById(R.id.button_currency_v2)
        listPopupWindowCurrency =
            ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowCurrency.anchorView = listPopupCurrencyButton
        listPopupWindowCurrency.width = maxWidth

        arrowForwardImage = view.findViewById(R.id.arrow_forward)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.d(TAG, "onViewCreated")

        super.onViewCreated(view, savedInstanceState)

        val transactionId = arguments?.getSerializable(ARG_TRANSACTION_ID) as UUID
        moneyTransactionDetailViewModel.loadTransaction(transactionId)

        val transactionLiveData = moneyTransactionDetailViewModel.transactionLiveData
        transactionLiveData.observe(
            viewLifecycleOwner
        ) { transaction ->
            transaction?.let {
                this.transactionWithProperties = transaction
                this.moneyTransaction = transactionWithProperties.transaction
                this.accountFrom = transactionWithProperties.accountFrom
                this.accountTo = transactionWithProperties.accountTo
                this.currency = transactionWithProperties.currency
                this.category = transactionWithProperties.category
                this.project = transactionWithProperties.project
                transactionGot = true
                updateUI()
                Log.d(TAG, "transaction got")
            }
        }

        val accountListViewModel by activityViewModels<AccountListViewModel>()
        accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner
        ) { accounts -> accounts?.let {
            updateAccountsList(accounts)
            updateUI()
        } }

        val currencyListViewModel by activityViewModels<CurrencyListViewModel>()
        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner
        ) { currencies -> currencies?.let {
            updateCurrenciesList(currencies)
            updateUI()
        } }

        val categoryListViewModel by activityViewModels<CategoryListViewModel>()

        categoryListViewModel.categoryListLiveData.observe(
            viewLifecycleOwner
        ) { categories -> categories?.let {
            updateCategoriesList(categories)
            updateUI()
        } }

        val projectListViewModel by activityViewModels<ProjectListViewModel>()

        projectListViewModel.projectListLiveData.observe(
            viewLifecycleOwner
        ) { projects -> projects?.let {
            updateProjectsList(projects)
            updateUI()
        } }

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

        if (transactionGot) {
            if (moneyTransaction.account_id_to == UUID.fromString(UUID_ACCOUNT_EMPTY)) {
                listPopupAccountToButton.visibility = View.GONE
                arrowForwardImage.visibility = View.GONE
            } else {
                listPopupAccountToButton.visibility = View.VISIBLE
                arrowForwardImage.visibility = View.VISIBLE
            }
        }

        if (transactionGot
            && accountsGot
            && currenciesGot
            && categoriesGot
            && projectsGot) {

            Log.d(TAG, "updateUI_2")

            amountField.setText(abs(moneyTransaction.amount_from).toString())
            dateButton.text = moneyTransaction.transactionDate.toString()

            updateAccountsList(accounts)
            updateCurrenciesList(currencies)
            updateCategoriesList(categories)
            updateProjectsList(projects)

            listPopupAccountFromButton.text = accountFrom.toString()
            listPopupAccountToButton.text = accountTo.toString()
            listPopupCategoryButton.text = category.toString()
            listPopupProjectButton.text = project.toString()
            listPopupCurrencyButton.text = currency.toString()

            setImageButton(revert = false)

        }
    }

    private fun updateAccountsList(accounts: List<Account>) {

        accountsGot = true

        this.accounts = accounts

        val accountsStrings = List(accounts.size)
        { i -> accounts[i].toString() }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, accountsStrings)

        listPopupWindowAccountFrom.setAdapter(adapter)
        listPopupWindowAccountFrom.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            moneyTransaction.account_id_from = accounts[position].account_id
            listPopupAccountFromButton.text = accounts[position].toString()
            listPopupWindowAccountFrom.dismiss()
        }

        listPopupAccountFromButton.setOnClickListener { v: View? -> listPopupWindowAccountFrom.show() }

        listPopupWindowAccountTo.setAdapter(adapter)
        listPopupWindowAccountTo.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
             moneyTransaction.account_id_to = accounts[position].account_id
            listPopupAccountToButton.text = accounts[position].toString()
             listPopupWindowAccountTo.dismiss()
        }

        listPopupAccountToButton.setOnClickListener { v: View? -> listPopupWindowAccountTo.show() }
    }

    private fun updateCurrenciesList(currencies: List<MainCurrency>) {

        Log.d(TAG, "updateCurrenciesList")

        currenciesGot = true

        this.currencies = currencies

        val currenciesStrings = List(currencies.size)
        { i -> currencies[i].toString() }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currenciesStrings)

        listPopupWindowCurrency.setAdapter(adapter)
        listPopupWindowCurrency.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            moneyTransaction.currency_id = currencies[position].currency_id
            listPopupCurrencyButton.text = currencies[position].toString()
            listPopupWindowCurrency.dismiss()
        }
       listPopupCurrencyButton.setOnClickListener { v: View? -> listPopupWindowCurrency.show() }
    }

    private fun updateCategoriesList(categories: List<Category>) {

        categoriesGot = true

        this.categories = categories

        Log.d(TAG, "updateCategoriesList")

        val categoriesStrings = List(categories.size)
        { i -> categories[i].toString() }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, categoriesStrings)

        listPopupWindowCategory.setAdapter(adapter)
        listPopupWindowCategory.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            moneyTransaction.category_id = categories[position].category_id
            listPopupCategoryButton.text = categories[position].toString()
            listPopupWindowCategory.dismiss()
        }

        listPopupCategoryButton.setOnClickListener { v: View? -> listPopupWindowCategory.show() }

    }

    private fun updateProjectsList(projects: List<Project>) {
        projectsGot = true
        this.projects = projects
        Log.d(TAG, "updateProjectsList")

        val projectsStrings = List(projects.size)
        { i -> projects[i].toString() }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, projectsStrings)

        listPopupWindowProject.setAdapter(adapter)
        listPopupWindowProject.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            moneyTransaction.project_id = projects[position].project_id
            listPopupProjectButton.text = projects[position].toString()
            listPopupWindowProject.dismiss()
        }
       listPopupProjectButton.setOnClickListener { v: View? -> listPopupWindowProject.show() }
    }

    override fun CloseWithoutSaving() {
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