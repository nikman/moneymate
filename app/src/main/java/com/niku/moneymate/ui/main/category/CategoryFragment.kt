package com.niku.moneymate.ui.main.category

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.niku.moneymate.R
import com.niku.moneymate.category.Category
import com.niku.moneymate.category.CategoryDetailViewModel
import com.niku.moneymate.utils.CategoryType
import com.niku.moneymate.utils.SharedPrefs
import java.util.*

private const val ARG_CATEGORY_ID = "category_id"
private const val TAG = "CategoryFragment"

class CategoryFragment: Fragment() {

    //private lateinit var viewModel: MainViewModel
    private lateinit var category: Category
    private lateinit var titleField: EditText
    private lateinit var typeField: EditText
    private lateinit var isDefaultCategoryCheckBox: CheckBox
    private lateinit var categoryTypeImageButton: ImageButton
    //private lateinit var noteField: EditText

    /*private val categoryDetailViewModel: CategoryDetailViewModel by lazy {
        ViewModelProvider(this)[CategoryDetailViewModel::class.java]
    }*/

    private val categoryDetailViewModel by activityViewModels<CategoryDetailViewModel>()

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
        typeField = view.findViewById(R.id.category_type)
        isDefaultCategoryCheckBox = view.findViewById(R.id.category_isDefault) as CheckBox
        categoryTypeImageButton = view.findViewById(R.id.image_button_category_type)

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
        /*viewModel =
            ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]*/
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val changedText = s.toString()
                category.category_title = changedText
            }
            override fun afterTextChanged(s: Editable?) { }
        }
        titleField.addTextChangedListener(titleWatcher)

        val typeWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //val changedText = s.toString()
                category.category_type = if (count > 0 && s.toString() != "-") s.toString().toInt() else 0
            }
            override fun afterTextChanged(s: Editable?) { }
        }
        typeField.addTextChangedListener(typeWatcher)

        isDefaultCategoryCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                //currency.currency_is_default = isChecked
                if (isChecked) {
                    SharedPrefs().storeCategoryId(context, category.category_id)
                }
            }
        }

        categoryTypeImageButton.apply {
            setOnClickListener { _, ->
                setImageButton(revert = true)
            }
        }

    }

    private fun setImageButton(revert: Boolean = false) {

        if (revert) {
            if (category.category_type == CategoryType.INCOME) {
                categoryTypeImageButton.setImageResource(R.drawable.ic_outcom_36)
                category.category_type = CategoryType.OUTCOME
            } else {
                categoryTypeImageButton.setImageResource(R.drawable.ic_income_36)
                category.category_type = CategoryType.INCOME
            }
        } else {
            if (category.category_type == CategoryType.INCOME) {
                categoryTypeImageButton.setImageResource(R.drawable.ic_income_36)
            } else {
                categoryTypeImageButton.setImageResource(R.drawable.ic_outcom_36)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        categoryDetailViewModel.saveCategory(category)
    }

    private fun updateUI() {
        titleField.setText(category.category_title)
        typeField.setText(category.category_type.toString())
        setImageButton(revert = false)

        val uuidAsString = context?.applicationContext?.let {
            SharedPrefs().getStoredCategoryId(it) }

        if (uuidAsString != null) {
            isDefaultCategoryCheckBox.isChecked =
                uuidAsString.isNotEmpty() &&
                        category.category_id == UUID.fromString(uuidAsString)
        }

    }

    companion object {
        fun newBundle(category_id: UUID) : Bundle {
            /*val args = Bundle().apply {
                putSerializable(ARG_CATEGORY_ID, category_id)
            }

            return CategoryFragment().apply {
                arguments = args
            }*/
            return Bundle().apply {
                putSerializable(ARG_CATEGORY_ID, category_id)
            }
        }
    }

}