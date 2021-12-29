package com.niku.moneymate.ui.main.project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.R
import com.niku.moneymate.currency.CurrencyDetailViewModel
import com.niku.moneymate.projects.Project
import com.niku.moneymate.ui.main.common.MainViewModel
import com.niku.moneymate.utils.SharedPrefs
import java.util.*

private const val ARG_PROJECT_ID = "project_id"

class ProjectFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var project: Project

    private lateinit var titleField: EditText
    private lateinit var isDefaultProjectCheckBox: CheckBox

    private val projectDetailViewModel: ProjectDetailViewModel by lazy {
        ViewModelProvider(this)[ProjectDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //currency = MainCurrency(643, "RUB", UUID.fromString("0f967f94-dca8-4e2a-8019-850b0dd9ea38"))
        project = Project()
        val projectId: UUID = arguments?.getSerializable(ARG_PROJECT_ID) as UUID
        projectDetailViewModel.loadProject(projectId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.currency_fragment, container, false)

        titleField = view.findViewById(R.id.project_title) as EditText
        isDefaultProjectCheckBox = view.findViewById(R.id.project_isDefault) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val currencyId = arguments?.getSerializable(ARG_CURRENCY_ID) as UUID

        currencyDetailViewModel.loadCurrency(currencyId)

        currencyDetailViewModel.currencyLiveData.observe(
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
                currency.currency_code = if (count > 0) s.toString().toInt() else 0
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

        isDefaultCurrencyCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                //currency.currency_is_default = isChecked
                if (isChecked) {
                    SharedPrefs().storeCurrencyId(context, currency.currency_id)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        currencyDetailViewModel.saveCurrency(currency)
    }

    private fun updateUI() {

        codeField.setText(currency.currency_code.toString())
        titleField.setText(currency.currency_title)

        val uuidAsString = context?.applicationContext?.let {
            SharedPrefs().getStoredCurrencyId(it) }

        if (uuidAsString != null) {
            isDefaultCurrencyCheckBox.isChecked =
                uuidAsString.isNotEmpty() &&
                        currency.currency_id == UUID.fromString(uuidAsString)
        }

    }

    companion object {
        /*fun newInstance(currency_id: UUID) : CurrencyFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CURRENCY_ID, currency_id)
            }
            return CurrencyFragment().apply {
                arguments = args
            }
        }*/
        fun newBundle(currency_id: UUID): Bundle {
            //return bundleOf("ARG_CURRENCY_ID" to currency_id)
            return Bundle().apply {
                putSerializable(ARG_CURRENCY_ID, currency_id)
            }
        }
    }

}