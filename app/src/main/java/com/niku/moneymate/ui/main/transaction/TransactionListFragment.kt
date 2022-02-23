package com.niku.moneymate.ui.main.transaction

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.R
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionListViewModel
import com.niku.moneymate.transaction.TransactionWithProperties
import com.niku.moneymate.ui.main.MateItemDecorator
import com.niku.moneymate.uiutils.BaseListItem
import com.niku.moneymate.uiutils.BaseSwipeHelper
import com.niku.moneymate.utils.*
import java.text.DateFormat
import java.util.*

private const val TAG = "TransactionListFragment"

class TransactionListFragment: Fragment() {

    interface Callbacks {
        fun onTransactionSelected(transactionId: UUID)
        fun onSettingsSelected()
    }

    private var callbacks: Callbacks? = null
    private lateinit var transactionRecyclerView: RecyclerView
    private var adapter: TransactionAdapter = TransactionAdapter(emptyList())

    private val transactionListViewModel by activityViewModels<TransactionListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_common_list, container, false)

        transactionRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        transactionRecyclerView.adapter = adapter

        transactionRecyclerView.addItemDecoration(
            MateItemDecorator(requireContext(), R.drawable.divider)
        )

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         BaseSwipeHelper<TransactionWithProperties>(requireContext())
            .setRecyclerView(transactionRecyclerView)
            .setDirection(ItemTouchHelper.LEFT)
            .setOnSwipeAction { transaction ->
                transactionListViewModel.deleteTransaction(
                    moneyTransaction = transaction.transaction)
            }
            .setOnUndoAction { transaction ->
                transactionListViewModel.addTransaction(
                    moneyTransaction = transaction.transaction)
            }
            .build()

        transactionListViewModel.transactionListLiveData.observe(
            viewLifecycleOwner
        ) { transactionWithProperties ->
            transactionWithProperties?.let { updateUI(transactionWithProperties) }
        }

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_transaction_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_transaction -> {

                val currency =
                    MainCurrency(
                        currency_id = UUID.fromString(getStoredCurrencyId(requireContext())))

                val category =
                    Category(
                        category_id = UUID.fromString(getStoredCategoryId(requireContext())))

                val account = Account(
                    currency_id = currency.currency_id,
                    account_id = UUID.fromString(getStoredAccountId(requireContext())))

                val transaction = MoneyTransaction(
                    account_id_from = account.account_id,
                    account_id_to = UUID.fromString(UUID_ACCOUNT_EMPTY),
                    currency_id = currency.currency_id,
                    category_id = category.category_id,
                    project_id = UUID.fromString(
                        getStoredProjectId(requireContext())))

                transactionListViewModel.addTransaction(transaction)
                callbacks?.onTransactionSelected(transaction.transaction_id)
                true
            }
            R.id.new_convertation -> {

                val currency =
                    MainCurrency(
                        currency_id = UUID.fromString(getStoredCurrencyId(requireContext())))

                val category =
                    Category(
                        category_id = UUID.fromString(getStoredCategoryId(requireContext())))

                val account = Account(
                    currency_id = currency.currency_id,
                    account_id = UUID.fromString(getStoredAccountId(requireContext())))

                val accountTo = Account(
                    currency_id = currency.currency_id,
                    account_id = UUID.fromString(getStoredAccountToId(requireContext())))

                val transaction = MoneyTransaction(
                    account_id_from = account.account_id,
                    account_id_to = accountTo.account_id,
                    currency_id = currency.currency_id,
                    category_id = category.category_id,
                    project_id = UUID.fromString(
                        getStoredProjectId(requireContext())))

                transactionListViewModel.addTransaction(transaction)
                callbacks?.onTransactionSelected(transaction.transaction_id)
                true
            }
            R.id.settings -> {
                callbacks?.onSettingsSelected()
                true
            }
            R.id.payees -> {
                //callbacks?.onPayeeSelected()
                requireActivity().findNavController(
                    R.id.bottomNavigationView).navigate(R.id.payeeListFragment)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(transactionWithProperties: List<TransactionWithProperties>) {

        Log.d(TAG, "updateUI")

        adapter = TransactionAdapter(transactionWithProperties)
        transactionRecyclerView.adapter = adapter

    }

    private inner class TransactionHolder(view: View):
        RecyclerView.ViewHolder(view),
        View.OnClickListener,
        View.OnLongClickListener,
        BaseListItem<TransactionWithProperties> {

        private lateinit var transaction: TransactionWithProperties

        private val dateTextView: TextView = itemView.findViewById(R.id.transaction_date)
        private val accountTextView: TextView = itemView.findViewById(R.id.transaction_account_title)
        private val amountTextView: TextView = itemView.findViewById(R.id.transaction_amount)
        private val categoryTextView: TextView = itemView.findViewById(R.id.transaction_category)
        private val projectTextView: TextView = itemView.findViewById(R.id.transaction_project)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun getItem(): TransactionWithProperties = transaction

        override fun onClick(v: View?) {
            callbacks?.onTransactionSelected(transaction.transaction.transaction_id)
        }

        override fun onLongClick(v: View?): Boolean {
            Toast.makeText(context, "Long click", Toast.LENGTH_LONG).show()
            return true
        }

        fun bind(transactionWithProperties: TransactionWithProperties) {

            this.transaction = transactionWithProperties

            dateTextView.text =
                DateFormat.getDateInstance().format(transaction.transaction.transactionDate)

            val accountFromText = transaction.accountFrom.toString()
            val itsTransfer = transaction.accountTo.account_id.toString() != UUID_ACCOUNT_EMPTY
            val accountToText = if (itsTransfer) transaction.accountTo.toString() else transaction.currency.toString()

            accountTextView.text =
                if (itsTransfer) {
                getString(R.string.transaction_list_convert_account_title,
                    accountFromText,
                    accountToText) } else { transaction.accountFrom.toString() }

            val trColor: String = when {
                itsTransfer -> "#F9A825"
                transaction.transaction.amount_from < 0 -> "#B71C1C"
                else -> "#1B5E20"
            }
            accountTextView.setTextColor(Color.parseColor(trColor))

            amountTextView.text = "%.2f".format(transaction.transaction.amount_from)
            this.transaction.project.let {
                projectTextView.text = it.project_title
            }
            categoryTextView.text = this.transaction.category.toString()

            amountTextView.setTextColor(Color.parseColor(trColor))

        }

    }

    private inner class TransactionAdapter(
        var transactionsWithProperties: List<TransactionWithProperties>):
            RecyclerView.Adapter<TransactionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_view_transaction, parent, false)

            return TransactionHolder(itemView)
        }

        override fun getItemCount(): Int {
            return transactionsWithProperties.size
        }

        override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
            val transactionWithProperties = transactionsWithProperties[position]
            holder.bind(transactionWithProperties)
        }

    }

}


