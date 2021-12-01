package com.niku.moneymate.ui.main.category

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.CommonViewModelFactory
import com.niku.moneymate.R
import com.niku.moneymate.category.Category
import com.niku.moneymate.category.CategoryListViewModel
import java.util.*

private const val TAG = "CategoryListFragment"

class CategoryListFragment: Fragment() {

    interface Callbacks {
        fun onCategorySelected(categoryId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var categoryRecyclerView: RecyclerView
    private var adapter: CategoryAdapter = CategoryAdapter(emptyList())

    private val viewModelFactory = CommonViewModelFactory()

    private val categoryListViewModel: CategoryListViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[CategoryListViewModel::class.java]
    }

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

        val view = inflater.inflate(R.layout.fragment_category_list, container, false)

        categoryRecyclerView = view.findViewById(R.id.category_recycler_view) as RecyclerView
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.adapter = adapter
        categoryRecyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
                .apply { setOrientation(1) }
        )

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryListViewModel.categoryListLiveData.observe(
            viewLifecycleOwner,
            Observer { categories -> categories?.let { updateUI(categories) } }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_category_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_category -> {
                Log.d(TAG,"new category pressed")
                val category = Category()
                categoryListViewModel.addCategory(category)
                callbacks?.onCategorySelected(category.category_id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(categories: List<Category>) {

        adapter = CategoryAdapter(categories)
        categoryRecyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        categoryListViewModel.categoryListLiveData.observe(
            viewLifecycleOwner,
            Observer { categories ->
                categories?.let {
                    Log.i(TAG, "Got categoryLiveData ${categories.size}")
                    for (element in categories) {
                        Log.i(TAG,
                            "Got elem ${element.category_title} # ${element.category_id}")
                    }
                    updateUI(categories)
                }
            }
        )

    }

    private inner class CategoryHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var category: Category

        private val titleTextView: TextView = itemView.findViewById(R.id.category_title)
        private val typeTextView: TextView = itemView.findViewById(R.id.category_type)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            //Toast.makeText(context, "${account.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCategorySelected(category.category_id)

        }

        fun bind(category: Category) {
            this.category = category
            titleTextView.text = this.category.category_title
            typeTextView.text = this.category.category_type.toString()
        }

    }

    private inner class CategoryAdapter(var categories: List<Category>): RecyclerView.Adapter<CategoryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_view_category, parent, false)

            return CategoryHolder(itemView)
        }

        override fun getItemCount() : Int {
            val categoriesSize = categories.size
            Log.d(TAG, "categories Size: $categoriesSize")
            return categoriesSize
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            //holder.apply { titleTextView.text = account.title }
            Log.d(TAG, "Position: $position")
            holder.bind(category)
        }

    }

    companion object {
        fun newInstance(): CategoryListFragment {return CategoryListFragment()
        }
    }

}