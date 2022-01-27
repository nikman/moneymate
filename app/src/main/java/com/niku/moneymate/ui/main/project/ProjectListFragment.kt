package com.niku.moneymate.ui.main.project

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.niku.moneymate.R
import com.niku.moneymate.projects.Project
import com.niku.moneymate.projects.ProjectListViewModel
import com.niku.moneymate.ui.main.MateItemDecorator
import com.niku.moneymate.utils.SharedPrefs
import java.util.*


private const val TAG = "ProjectListFragment"

class ProjectListFragment: Fragment() {

    interface Callbacks {
        fun onProjectSelected(projectId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var projectRecyclerView: RecyclerView
    private var adapter: ProjectAdapter = ProjectAdapter(emptyList())

    private val projectListViewModel by activityViewModels<ProjectListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_common_list, container, false)

        projectRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        projectRecyclerView.layoutManager = LinearLayoutManager(context)
        projectRecyclerView.adapter = adapter
        projectRecyclerView.addItemDecoration(
            MateItemDecorator(requireContext(), R.drawable.divider)
        )

        // swipe actions
        val trashBinIcon =
            ResourcesCompat.getDrawable(resources,
                R.drawable.ic_baseline_delete_forever_24, null)

        val swipeRightCallback = object: ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete project")

                // Display a message on alert dialog
                builder.setMessage("Are you want to delete project?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES"){ _, _ ->
                    val projectsAdapter: ProjectAdapter = viewHolder.bindingAdapter as ProjectAdapter
                    val project = projectsAdapter.adapterProjects[viewHolder.bindingAdapterPosition]
                    projectListViewModel.deleteProject(project)
                    /////////////
                    // showing snack bar with Undo option
                    // showing snack bar with Undo option
                    val snackbar: Snackbar = Snackbar
                        .make(
                            view,
                            "${project.project_title} removed!",
                            Snackbar.LENGTH_LONG
                        )
                    snackbar.setAction("UNDO", View.OnClickListener
                    { _, ->
                        projectListViewModel.addProject(project = project)
                    })

                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                    /////////////
                }

                // Display a negative button on alert dialog
                builder.setNegativeButton("No"){ _, _ ->

                }

                // Display a neutral button on alert dialog
                builder.setNeutralButton("Cancel"){_,_ ->
                    //Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
                }

                val dialog: AlertDialog = builder.create()

                dialog.show()

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                c.clipRect(
                    viewHolder.itemView.right.toFloat() + dX,
                    viewHolder.itemView.top.toFloat(),
                    viewHolder.itemView.right.toFloat(),
                    viewHolder.itemView.bottom.toFloat())
                c.drawColor(Color.RED)

                trashBinIcon?.let {
                    it.bounds =
                        Rect(
                            viewHolder.itemView.right - trashBinIcon.intrinsicWidth - 16,
                            viewHolder.itemView.top + 32,
                            viewHolder.itemView.right - 16,
                            viewHolder.itemView.top + trashBinIcon.intrinsicHeight + 32
                        )
                    it.draw(c)
                }
            }
        }

        val swipeRightHelper = ItemTouchHelper(swipeRightCallback)
        swipeRightHelper.attachToRecyclerView(projectRecyclerView)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectListViewModel.projectListLiveData.observe(
            viewLifecycleOwner,
            Observer { projects -> projects?.let { updateUI(projects) } }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_project_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_project -> {
                Log.d(TAG,"new project pressed")

                val project = Project()

                projectListViewModel.addProject(project)
                callbacks?.onProjectSelected(project.project_id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(projects: List<Project>) {

        adapter = ProjectAdapter(projects)
        projectRecyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        projectListViewModel.projectListLiveData.observe(
            viewLifecycleOwner,
            Observer { projects ->
                projects?.let {
                    Log.i(TAG, "Got projectLiveData ${projects.size}")
                    for (element in projects) {
                        Log.i(
                            TAG,
                            "Got elem ${element.project_title} # ${element.project_id}")
                    }
                    updateUI(projects)
                }
            }
        )

    }

    private inner class ProjectHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var project: Project

        private val titleTextView: TextView = itemView.findViewById(R.id.project_title)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            callbacks?.onProjectSelected(project.project_id)

        }

        fun bind(project: Project) {
            this.project = project
            titleTextView.text = this.project.project_title

            val uuidAsString = context?.applicationContext?.let {
                SharedPrefs().getStoredProjectId(it) }

            if (uuidAsString != null) {
                if (uuidAsString.isNotEmpty() &&
                    project.project_id == UUID.fromString(uuidAsString)) {
                        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        titleTextView.typeface = boldTypeface
                }
            }

        }

    }

    private inner class ProjectAdapter(var projects: List<Project>): RecyclerView.Adapter<ProjectHolder>() {

        val adapterProjects = projects

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {

            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_view_project, parent, false)

            return ProjectHolder(itemView)
        }

        override fun getItemCount() : Int {
            val projectsSize = projects.size
            Log.d(TAG, "projects Size: $projectsSize")
            return projectsSize
        }

        override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
            val project = projects[position]

            Log.d(TAG, "Position: $position")
            holder.bind(project)
        }

    }

    companion object {
        fun newInstance(): ProjectListFragment {return ProjectListFragment()}
    }
}


