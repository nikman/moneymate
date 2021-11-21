package com.niku.moneymate.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.niku.moneymate.account.Account
import com.niku.moneymate.category.Category
import com.niku.moneymate.ui.main.account.MainViewModel

private const val ARG_CATEGORY_ID = "category_id"
private const val TAG = "CategoryFragment"

class CategoryFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var category: Category
    private lateinit var titleField: EditText
    //private lateinit var noteField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}