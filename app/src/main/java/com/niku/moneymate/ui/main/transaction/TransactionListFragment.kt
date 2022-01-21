package com.niku.moneymate.ui.main.transaction

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.account.Account
import com.niku.moneymate.R
import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.projects.Project
import com.niku.moneymate.transaction.MoneyTransaction
import com.niku.moneymate.transaction.TransactionListViewModel
import com.niku.moneymate.transaction.TransactionWithProperties
import com.niku.moneymate.ui.main.MateItemDecorator
import com.niku.moneymate.utils.SharedPrefs
import com.niku.moneymate.utils.UUID_ACCOUNT_EMPTY
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
                //Log.d(TAG,"new transaction pressed")

                val currency =
                    MainCurrency(
                        currency_id = UUID.fromString(SharedPrefs().getStoredCurrencyId(requireContext())))

                val category =
                    Category(
                        category_id = UUID.fromString(SharedPrefs().getStoredCategoryId(requireContext())))

                val account = Account(
                    currency_id = currency.currency_id,
                    account_id = UUID.fromString(SharedPrefs().getStoredAccountId(requireContext())))

                val project = Project(
                    project_id = UUID.fromString(SharedPrefs().getStoredProjectId(requireContext()))
                )

                val transaction = MoneyTransaction(
                    account.account_id,
                    //account.account_id,
                    UUID.fromString(UUID_ACCOUNT_EMPTY),
                    currency.currency_id,
                    category.category_id,
                    UUID.fromString(
                        SharedPrefs().getStoredProjectId(requireContext())))

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
                requireActivity().findNavController(R.id.bottomNavigationView).navigate(R.id.payeeListFragment)
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
        private val categoryTextView: TextView = itemView.findViewById(R.id.transaction_category)
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
            amountTextView.text = "%.2f".format(this.transaction.transaction.amount_from)
            this.transaction.project.let {
                projectTextView.text = it.project_title
            }
            categoryTextView.text = this.transaction.category.category_title

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
            //Log.d(TAG, "Position: $position")
            holder.bind(transactionWithProperties)
        }

    }

    companion object {
        fun newInstance(): TransactionListFragment {return TransactionListFragment()}
    }
}


