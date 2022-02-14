package com.niku.moneymate.ui.main.currency

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.R
import com.niku.moneymate.currency.CurrencyListViewModel
import com.niku.moneymate.currency.MainCurrency
import com.niku.moneymate.ui.main.MateItemDecorator
import com.niku.moneymate.uiutils.BaseSwipeHelper
import com.niku.moneymate.utils.getStoredCurrencyId
import java.util.*


private const val TAG = "CurrencyListFragment"

class CurrencyListFragment: Fragment() {

    interface Callbacks {
        fun onCurrencySelected(currencyId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var currencyRecyclerView: RecyclerView
    private var adapter: CurrencyAdapter = CurrencyAdapter(emptyList())

    private val currencyListViewModel by activityViewModels<CurrencyListViewModel>()

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

        currencyRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        currencyRecyclerView.layoutManager = LinearLayoutManager(context)
        currencyRecyclerView.adapter = adapter
        currencyRecyclerView.addItemDecoration(
            MateItemDecorator(requireContext(), R.drawable.divider)
        )

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner
        ) { currencies -> currencies?.let { updateUI(currencies) } }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_currency_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_currency -> {
                Log.d(TAG,"new currency pressed")
                val currency = MainCurrency()
                currencyListViewModel.addCurrency(currency)
                callbacks?.onCurrencySelected(currency.currency_id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(currencies: List<MainCurrency>) {

        adapter = CurrencyAdapter(currencies)
        currencyRecyclerView.adapter = adapter

        /*val sw: BaseSwipeHelper<MainCurrency> = BaseSwipeHelper()
        sw.onSwipeItem(
            currencies,
            requireContext(),
            currencyRecyclerView,
            ItemTouchHelper.LEFT,
            { currency -> currencyListViewModel.deleteCurrency(currency = currency) },
            { currency -> currencyListViewModel.addCurrency(currency = currency) })*/

        val sw: BaseSwipeHelper<MainCurrency> = BaseSwipeHelper<MainCurrency>(requireContext())
            .setRecyclerView(currencyRecyclerView)
            .setDirection(ItemTouchHelper.LEFT)
            .setItems(currencies)
            .setOnSwipeAction { currency -> currencyListViewModel.deleteCurrency(currency = currency) }
            .setOnCancelAction { currency -> currencyListViewModel.addCurrency(currency = currency) }
            .build()

    }

    override fun onStart() {
        super.onStart()

        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner
        ) { currencies ->
            currencies?.let {
                Log.i(TAG, "Got currencyLiveData ${currencies.size}")
                for (element in currencies) {
                    Log.i(
                        TAG,
                        "Got elem ${element.currency_title} # ${element.currency_id}"
                    )
                }
                updateUI(currencies)
            }
        }
    }

    private inner class CurrencyHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var currency: MainCurrency

        private val titleTextView: TextView = itemView.findViewById(R.id.currency_title)
        private val codeTextView: TextView = itemView.findViewById(R.id.currency_code)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            //Toast.makeText(context, "${account.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCurrencySelected(currency.currency_id)

        }

        fun bind(currency: MainCurrency) {
            this.currency = currency
            titleTextView.text = this.currency.currency_title
            codeTextView.text = this.currency.currency_code.toString()

            val uuidAsString = getStoredCurrencyId(requireContext())

            if (uuidAsString != null) {
                if (uuidAsString.isNotEmpty() &&
                            currency.currency_id == UUID.fromString(uuidAsString)) {
                    val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    titleTextView.typeface = boldTypeface
                    codeTextView.typeface = boldTypeface
                }
            }

        }

    }

    private inner class CurrencyAdapter(var currencies: List<MainCurrency>): RecyclerView.Adapter<CurrencyHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
            //val view = layoutInflater.inflate(R.layout.list_item_view_account, parent, false)
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_view_currency, parent, false)

            return CurrencyHolder(itemView)
        }

        override fun getItemCount() : Int {
            val currenciesSize = currencies.size
            Log.d(TAG, "currencies Size: $currenciesSize")
            return currenciesSize
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
            val currency = currencies[position]
            //holder.apply { titleTextView.text = account.title }
            Log.d(TAG, "Position: $position")
            holder.bind(currency)
        }

    }

    companion object {
        fun newInstance(): CurrencyListFragment {return CurrencyListFragment()}
    }
}


