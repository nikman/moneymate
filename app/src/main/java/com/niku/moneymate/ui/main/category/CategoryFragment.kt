package com.niku.moneymate.ui.main.category

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.niku.moneymate.R
import com.niku.moneymate.category.Category
import com.niku.moneymate.category.CategoryDetailViewModel
import com.niku.moneymate.ui.main.common.MainViewModel
import java.util.*

private const val ARG_CATEGORY_ID = "category_id"
private const val TAG = "CategoryFragment"

class CategoryFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var category: Category
    private lateinit var titleField: EditText
    //private lateinit var noteField: EditText

    private val categoryDetailViewModel: CategoryDetailViewModel by lazy {
        ViewModelProvider(this)[CategoryDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = Category()
        val categoryId: UUID = arguments?.getSerializable(ARG_CATEGORY_ID) as UUID
        categoryDetailViewModel.loadCategory(categoryId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.category_fragment, container, false)
        titleField = view.findViewById(R.id.category_title)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId: UUID = arguments?.getSerializable(ARG_CATEGORY_ID) as UUID
        categoryDetailViewModel.loadCategory(categoryId)

        categoryDetailViewModel.categoryLiveData.observe(
            viewLifecycleOwner,
            {
                category -> category?.let {
                    this.category = category
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

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val changedText = s.toString()

                category.title = changedText

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)

    }

    override fun onStop() {
        super.onStop()
        categoryDetailViewModel.saveCategory(category)
    }

    private fun updateUI() {
        titleField.setText(category.title)
    }

    companion object {
        fun newInstance(category_id: UUID) : CategoryFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CATEGORY_ID, category_id)
            }

            return CategoryFragment().apply {
                arguments = args
            }
        }
    }

}