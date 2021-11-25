package com.niku.moneymate.ui.main.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.R

class CurrencyFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyFragment()
    }

    //private lateinit var viewModel: MainViewModel
    private lateinit var currency: MainCurrency
    private lateinit var titleField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currency = MainCurrency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.currency_fragment, container, false)

        titleField = view.findViewById(R.id.currency_title) as EditText

        return view
    }

    override fun onStart() {
        super.onStart()

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
}