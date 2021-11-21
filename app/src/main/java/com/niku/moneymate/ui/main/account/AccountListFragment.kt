package com.niku.moneymate.ui.main.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.account.Account
import com.niku.moneymate.account.AccountListViewModel
import com.niku.moneymate.account.AccountViewModelFactory
import com.niku.moneymate.R
import java.util.*

private const val TAG = "AccountListFragment"

class AccountListFragment: Fragment() {

    interface Callbacks {
        fun onAccountSelected(accountId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var accountRecyclerView: RecyclerView
    private var adapter: AccountAdapter = AccountAdapter(emptyList())

    private val viewModelFactory = AccountViewModelFactory()

    private val accountListViewModel: AccountListViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[AccountListViewModel::class.java]
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

        val view = inflater.inflate(R.layout.fragment_account_list, container, false)

        accountRecyclerView = view.findViewById(R.id.account_recycler_view) as RecyclerView
        accountRecyclerView.layoutManager = LinearLayoutManager(context)
        accountRecyclerView.adapter = adapter
        accountRecyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
                .apply { setOrientation(1) }
        )

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner,
            Observer { accounts -> accounts?.let { updateUI(accounts) } }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_account_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_account -> {
                Log.d(TAG,"new account pressed")
                val account = Account()
                accountListViewModel.addAccount(account)
                callbacks?.onAccountSelected(account.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(accounts: List<Account>) {

        adapter = AccountAdapter(accounts)
        accountRecyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner,
            Observer { accounts ->
                accounts?.let {
                    Log.i(TAG, "Got accountLiveData ${accounts.size}")
                    for (element in accounts) {
                        Log.i(TAG, "Got elem ${element.title} # ${element.id} note: ${element.note}")
                    }
                    updateUI(accounts)
                }
            }
        )

    }

    private inner class AccountHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var account: Account

        private val titleTextView: TextView = itemView.findViewById(R.id.account_title)
        private val noteTextView: TextView = itemView.findViewById(R.id.account_note)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            //Toast.makeText(context, "${account.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onAccountSelected(account.id)

        }

        fun bind(account: Account) {
            this.account = account
            titleTextView.text = this.account.title
            noteTextView.text = this.account.note
        }

    }

    private inner class AccountAdapter(var accounts: List<Account>): RecyclerView.Adapter<AccountHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
            //val view = layoutInflater.inflate(R.layout.list_item_view_account, parent, false)
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_view_account, parent, false)

            return AccountHolder(itemView)
        }

        override fun getItemCount() : Int {
            val accSize = accounts.size
            Log.d(TAG, "accSize: $accSize")
            return accSize
        }

        override fun onBindViewHolder(holder: AccountHolder, position: Int) {
            val account = accounts[position]
            //holder.apply { titleTextView.text = account.title }
            Log.d(TAG, "Position: $position")
            holder.bind(account)
        }

    }

    companion object {
        fun newInstance(): AccountListFragment {return AccountListFragment()}
    }
}


