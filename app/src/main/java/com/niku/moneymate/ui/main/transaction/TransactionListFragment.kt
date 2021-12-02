package com.niku.moneymate.ui.main.transaction

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.CommonViewModelFactory
import com.niku.moneymate.account.Account
import com.niku.moneymate.R
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
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
        /*accountRecyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
                .apply { setOrientation(1) }
        )*/

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
            R.id.new_account -> {
                Log.d(TAG,"new account pressed")
                //val currency = MainCurrency(643, "RUB", UUID.fromString("0f967f94-dca8-4e2a-8019-850b0dd9ea38"))

                val currency = MainCurrency(
                        UUID.fromString(
                            context?.applicationContext?.let {
                                SharedPrefs().getStoredCurrencyId(it) }))
                val category = Category(0, "",
                    UUID.fromString(
                        context?.applicationContext?.let {
                            SharedPrefs().getStoredCategoryId(it) }))

                val account = Account(currency.currency_id)
                val transaction = MoneyTransaction(
                    account.account_id, currency.currency_id, category.category_id)
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
                            "Got elem ${element.account.title} # ${element.account.account_id} ${element.transaction.amount} currency: ${element.currency.currency_title}")
                    }
                    updateUI(transactions)
                }
            }
        )

    }

    private inner class TransactionHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var transaction: TransactionWithProperties

        private val dateButton: Button = itemView.findViewById(R.id.transaction_date)
        private val amountTextView: TextView = itemView.findViewById(R.id.transaction_amount)
        //private val noteTextView: TextView = itemView.findViewById(R.id.account_note)
        //private val currencySpinner: Spinner = itemView.findViewById(R.id.tra)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            //Toast.makeText(context, "${account.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onTransactionSelected(transaction.transaction.transaction_id)

        }

        fun bind(transactionWithProperties: TransactionWithProperties) {
            this.transaction = transactionWithProperties
            //titleTextView.text = this.account.account.title
            //noteTextView.text = this.account.account.note
            //currencyTextView.text = this.account.currency.currency_title
            amountTextView.text = this.transaction.transaction.amount.toString()
        }

    }

    private inner class TransactionAdapter(var transactionsWithProperties: List<TransactionWithProperties>): RecyclerView.Adapter<TransactionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
            //val view = layoutInflater.inflate(R.layout.list_item_view_account, parent, false)
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

