package com.niku.moneymate.ui.main.project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.niku.moneymate.R
import com.niku.moneymate.projects.Project
import com.niku.moneymate.projects.ProjectDetailViewModel
import com.niku.moneymate.utils.getStoredProjectId
import com.niku.moneymate.utils.storeProjectId
import java.util.*

private const val ARG_PROJECT_ID = "project_id"

class ProjectFragment : Fragment() {

    //private lateinit var viewModel: MainViewModel
    private lateinit var project: Project

    private lateinit var titleField: EditText
    private lateinit var isDefaultProjectCheckBox: CheckBox

    /*private val projectDetailViewModel: ProjectDetailViewModel by lazy {
        ViewModelProvider(this)[ProjectDetailViewModel::class.java]
    }*/

    private val projectDetailViewModel by activityViewModels<ProjectDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        project = Project()
        val projectId: UUID = arguments?.getSerializable(ARG_PROJECT_ID) as UUID
        projectDetailViewModel.loadProject(projectId)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.project_fragment, container, false)

        titleField = view.findViewById(R.id.project_title) as EditText
        isDefaultProjectCheckBox = view.findViewById(R.id.project_isDefault) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val projectId = arguments?.getSerializable(ARG_PROJECT_ID) as UUID

        projectDetailViewModel.loadProject(projectId)

        projectDetailViewModel.projectLiveData.observe(
            viewLifecycleOwner,
            {
                    project -> project?.let {
                this.project = project
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

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                project.project_title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)

        isDefaultProjectCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    storeProjectId(context, project.project_id)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        projectDetailViewModel.saveProject(project)
    }

    private fun updateUI() {

        titleField.setText(project.project_title)

        val uuidAsString = getStoredProjectId(requireContext())

        if (uuidAsString != null) {
            isDefaultProjectCheckBox.isChecked =
                uuidAsString.isNotEmpty() &&
                        project.project_id == UUID.fromString(uuidAsString)
        }

    }

    companion object {

        fun newBundle(project_id: UUID): Bundle {
            return Bundle().apply {
                putSerializable(ARG_PROJECT_ID, project_id)
            }
        }
    }

}