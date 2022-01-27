package com.niku.moneymate.ui.main.payee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.niku.moneymate.payee.PayeeListViewModel

private const val TAG = "PayeeListFragment"

class PayeeListFragment: Fragment() {

    private val payeeListViewModel by activityViewModels<PayeeListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        /*setContent {
            Text(text = "Hello")
        */
        /*val binding =
            PayeeListFragmentBinding.inflate(inflater, container, false)
                .apply {
                    this.vm = payeeListViewModel
                }
        return binding.root*/
    }

    /*@Composable
    fun MyExample(payeeListViewModel: PayeeListViewModel) {
        val dataExample = payeeListViewModel.payeeListLiveData.observeAsState()
        dataExample.value?.let {
            //ShowData(dataExample)
            Text(dataExample.value.toString())
        }
    }*/

    companion object {
        fun newInstance(): PayeeListFragment {return PayeeListFragment()}
    }
}


