package com.niku.moneymate.projects

import androidx.lifecycle.ViewModel
import com.niku.moneymate.database.MoneyMateRepository

class ProjectListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val projectListLiveData = moneyMateRepository.getProjects()

    fun addProject(project: Project) {
        moneyMateRepository.addProject(project)
    }

    fun deleteProject(project: Project) {
        moneyMateRepository.deleteProject(project)
    }

}