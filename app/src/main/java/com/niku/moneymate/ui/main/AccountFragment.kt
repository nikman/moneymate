package com.niku.moneymate.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.niku.moneymate.Account
import com.niku.moneymate.AccountDetailViewModel
import com.niku.moneymate.R
import java.util.*

private const val ARG_ACCOUNT_ID = "account_id"
private const val TAG = "AccountFragment"

class AccountFragment : Fragment() {

    /*companion object {
        fun newInstance() = AccountFragment()
    }*/

    companion object {
        fun newInstance(account_id: UUID) : AccountFragment {
            val args = Bundle().apply {
                putSerializable(ARG_ACCOUNT_ID, account_id)
            }
            return AccountFragment().apply {
                arguments = args
            }
        }
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var account: Account
    private lateinit var titleField: EditText

    private val accountDetailViewModel: AccountDetailViewModel by lazy {
        ViewModelProvider(this)[AccountDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = Account()
        val accountId: UUID = arguments?.getSerializable(ARG_ACCOUNT_ID) as UUID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.account_fragment, container, false)

        titleField = view.findViewById(R.id.account_title) as EditText

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountDetailViewModel.accountLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                account -> account?.let {
                    this.account = account
                    updateUI()
            }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                account.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)

    }

    override fun onStop() {
        super.onStop()
        accountDetailViewModel.saveAccount(account)
    }

    private fun updateUI() {

        titleField.setText(account.title)

    }

}