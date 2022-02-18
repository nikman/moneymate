package com.niku.moneymate.ui.main.project

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niku.moneymate.R
import com.niku.moneymate.projects.Project
import com.niku.moneymate.projects.ProjectListViewModel
import com.niku.moneymate.ui.main.MateItemDecorator
import com.niku.moneymate.uiutils.BaseListItem
import com.niku.moneymate.uiutils.BaseSwipeHelper
import com.niku.moneymate.utils.UUID_PROJECT_EMPTY
import com.niku.moneymate.utils.getStoredProjectId
import com.niku.moneymate.utils.storeProjectId
import java.util.*


private const val TAG = "ProjectListFragment"

class ProjectListFragment: Fragment() {

    interface Callbacks {
        fun onProjectSelected(projectId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var projectRecyclerView: RecyclerView
    private var adapter: ProjectAdapter = ProjectAdapter(emptyList())
    //private lateinit var swipeActions: BaseSwipeHelper<Project>

    private val projectListViewModel by activityViewModels<ProjectListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        //swipeAction = context as SwipeAction?
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

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BaseSwipeHelper<Project>(requireContext())
            .setRecyclerView(projectRecyclerView)
            .setDirection(ItemTouchHelper.LEFT)
            .setOnSwipeAction { project ->
                val defaultProjectUUID =
                    UUID.fromString(getStoredProjectId(requireContext()))
                if (project.project_id == defaultProjectUUID) {
                    storeProjectId(requireContext(), UUID.fromString(UUID_PROJECT_EMPTY))
                }
                projectListViewModel.deleteProject(project = project)
            }
            .setOnUndoAction { project -> projectListViewModel.addProject(project = project) }
            .build()

        projectListViewModel.projectListLiveData.observe(
            viewLifecycleOwner
        ) { projects -> projects?.let { updateUI(projects) } }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        //swipeAction = null
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
        RecyclerView.ViewHolder(view),
        View.OnClickListener,
        BaseListItem<Project> {

        private lateinit var project: Project

        override fun getItem(): Project = project

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

            val uuidAsString = getStoredProjectId(requireContext())

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

        //val adapterProjects = projects

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


