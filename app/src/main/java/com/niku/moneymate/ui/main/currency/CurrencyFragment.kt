package com.niku.moneymate.ui.main.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.R
import com.niku.moneymate.currency.CurrencyDetailViewModel
import com.niku.moneymate.ui.main.common.MainViewModel
import java.util.*

private const val ARG_CURRENCY_ID = "currency_id"

class CurrencyFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var currency: MainCurrency

    private lateinit var codeField: EditText
    private lateinit var titleField: EditText

    private val currencyDetailViewModel: CurrencyDetailViewModel by lazy {
        ViewModelProvider(this)[CurrencyDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //currency = MainCurrency(643, "RUB", UUID.fromString("0f967f94-dca8-4e2a-8019-850b0dd9ea38"))
        currency = MainCurrency()
        val currencyId: UUID = arguments?.getSerializable(ARG_CURRENCY_ID) as UUID
        currencyDetailViewModel.loadCurrency(currencyId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.currency_fragment, container, false)

        codeField = view.findViewById(R.id.currency_code) as EditText
        titleField = view.findViewById(R.id.currency_title) as EditText

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val currencyId = arguments?.getSerializable(ARG_CURRENCY_ID) as UUID

        currencyDetailViewModel.loadCurrency(currencyId)

        val currencyObserver = currencyDetailViewModel.currencyLiveData.observe(
            viewLifecycleOwner,
            {
                    currency -> currency?.let {
                this.currency = currency
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

        val codeWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currency.currency_code = s.toString().toInt()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        codeField.addTextChangedListener(codeWatcher)

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currency.currency_title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)

    }

    override fun onStop() {
        super.onStop()
        currencyDetailViewModel.saveCurrency(currency)
    }

    private fun updateUI() {

        codeField.setText(currency.currency_code.toString())
        titleField.setText(currency.currency_title)

    }

    companion object {
        fun newInstance(currency_id: UUID) : CurrencyFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CURRENCY_ID, currency_id)
            }
            return CurrencyFragment().apply {
                arguments = args
            }
        }
    }

}