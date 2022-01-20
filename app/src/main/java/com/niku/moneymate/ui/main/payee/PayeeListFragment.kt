package com.niku.moneymate.ui.main.payee

import android.os.Bundle
import android.view.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.niku.moneymate.databinding.PayeeListFragmentBinding
import com.niku.moneymate.payee.PayeeListViewModel

private const val TAG = "PayeeListFragment"

class PayeeListFragment: Fragment() {

    private val payeeListViewModel by activityViewModels<PayeeListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        /*setContent {
            Text(text = "Hello")
        */
        val binding =
            PayeeListFragmentBinding.inflate(inflater, container, false)
                .apply {
                    this.vm = payeeListViewModel
                }
        return binding.root
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


