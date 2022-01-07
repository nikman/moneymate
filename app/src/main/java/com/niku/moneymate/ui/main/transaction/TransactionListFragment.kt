package com.niku.moneymate.ui.main.transaction

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.CommonViewModelFactory
import com.niku.moneymate.account.Account
import com.niku.moneymate.R
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionListViewModel
import com.niku.moneymate.transaction.TransactionWithProperties
import com.niku.moneymate.utils.SharedPrefs
import java.util.*

private const val TAG = "TransactionListFragment"

class TransactionListFragment: Fragment() {

    interface Callbacks {
        fun onTransactionSelected(transactionId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var transactionRecyclerView: RecyclerView
    private var adapter: TransactionAdapter = TransactionAdapter(emptyList())

    private val viewModelFactory = CommonViewModelFactory()

    private val transactionListViewModel: TransactionListViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[TransactionListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total accounts: ${accountListViewModel.accounts.size}")
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_common_list, container, false)

        transactionRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        transactionRecyclerView.adapter = adapter

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionListViewModel.transactionListLiveData.observe(
            viewLifecycleOwner,
            Observer { transactionWithProperties ->
                transactionWithProperties?.let { updateUI(transactionWithProperties) } }
        )
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
                Log.d(TAG,"new transaction pressed")

                val currency = MainCurrency(
                        UUID.fromString(
                            context?.applicationContext?.let {
                                SharedPrefs().getStoredCurrencyId(it) }))

                val category = Category(0, "",
                    UUID.fromString(
                        context?.applicationContext?.let {
                            SharedPrefs().getStoredCategoryId(it) }))

                val account = Account(
                    currency.currency_id,
                    "",
                    0.0,
                    0.0,
                    "",
                    UUID.fromString(
                        SharedPrefs().getStoredAccountId(requireContext())))

                val project = Project(
                    "",
                    UUID.fromString(
                        SharedPrefs().getStoredProjectId(requireContext()))
                )

                val transaction = MoneyTransaction(
                    account.account_id,
                    account.account_id,
                    currency.currency_id,
                    category.category_id,
                    UUID.fromString(
                        SharedPrefs().getStoredProjectId(requireContext())))

                transactionListViewModel.addTransaction(transaction)
                callbacks?.onTransactionSelected(transaction.transaction_id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(transactionWithProperties: List<TransactionWithProperties>) {

        adapter = TransactionAdapter(transactionWithProperties)
        transactionRecyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        transactionListViewModel.transactionListLiveData.observe(
            viewLifecycleOwner,
            Observer { transactions -> transactions?.let {
                    Log.i(TAG, "Got transactionLiveData ${transactions.size}")
                    for (element in transactions) {
                        Log.i(
                            TAG,
                            "Got elem ${element.accountFrom.title} # ${element.accountFrom.account_id} ${element.transaction.amount_from} currency: ${element.currency.currency_title}")
                    }
                    updateUI(transactions)
                }
            }
        )

    }

    private inner class TransactionHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        private lateinit var transaction: TransactionWithProperties

        private val dateTextView: TextView = itemView.findViewById(R.id.transaction_date)
        private val accountTextView: TextView = itemView.findViewById(R.id.transaction_account_title)
        private val amountTextView: TextView = itemView.findViewById(R.id.transaction_amount)
        private val projectTextView: TextView = itemView.findViewById(R.id.transaction_project)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacks?.onTransactionSelected(transaction.transaction.transaction_id)
        }

        override fun onLongClick(v: View?): Boolean {
            Toast.makeText(context, "Long click", Toast.LENGTH_LONG).show()
            return true
        }

        fun bind(transactionWithProperties: TransactionWithProperties) {

            this.transaction = transactionWithProperties

            dateTextView.text = this.transaction.transaction.transactionDate.toString()
            accountTextView.text = this.transaction.accountFrom.title
            amountTextView.text = this.transaction.transaction.amount_from.toString()
            projectTextView.text = this.transaction.project.project_title

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

        override fun getItemCount() : Int {
            val trSize = transactionsWithProperties.size
            Log.d(TAG, "trSize: $trSize")
            return trSize
        }

        override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
            val transactionWithProperties = transactionsWithProperties[position]
            //holder.apply { titleTextView.text = account.title }
            Log.d(TAG, "Position: $position")
            holder.bind(transactionWithProperties)
        }

    }

    companion object {
        fun newInstance(): TransactionListFragment {return TransactionListFragment()}
    }
}


