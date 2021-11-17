package com.niku.moneymate.ui.main

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
import com.niku.moneymate.Account
import com.niku.moneymate.AccountListViewModel
import com.niku.moneymate.AccountViewModelFactory
import com.niku.moneymate.R
import java.util.*

private const val TAG = "AccountListFragment"

class AccountListFragment: Fragment() {

    interface Callbacks {
        fun onAccountSelected(accountId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var accountRecyclerView: RecyclerView
    private var adapter: AccountAdapter? = AccountAdapter(emptyList())

    /*private val accountListViewModel: AccountListViewModel by lazy {
        ViewModelProvider(this).get(AccountListViewModel::class.java)
    }*/

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

        //updateUI()

        return view

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
                val account = Account()
                accountListViewModel.addAccount(account)
                callbacks?.onAccountSelected(account.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(accounts: List<Account>) {
        //val accounts = accountListViewModel.accounts
        adapter = AccountAdapter(accounts)
        accountRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountListViewModel.accountListLiveData.observe(
            viewLifecycleOwner,
            Observer { accounts -> accounts?.let { updateUI(accounts) } }
        )
    }

    private inner class AccountHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var account: Account

        private val titleTextView: TextView = itemView.findViewById(R.id.account_title)

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
        }

    }

    private inner class AccountAdapter(val accounts: List<Account>): RecyclerView.Adapter<AccountHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
            val view = layoutInflater.inflate(R.layout.list_item_view_account, parent, false)
            return AccountHolder(view)
        }

        override fun getItemCount() = accounts.size

        override fun onBindViewHolder(holder: AccountHolder, position: Int) {
            val account = accounts[position]
            //holder.apply { titleTextView.text = account.title }
            holder.bind(account)
        }
    }

    companion object {
        fun newInstance(): AccountListFragment {return AccountListFragment()}
    }
}


