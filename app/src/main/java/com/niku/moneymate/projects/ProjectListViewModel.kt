package com.niku.moneymate.projects

import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository

class ProjectListViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    val projectListLiveData = moneyMateRepository.getProjects()

    fun addCurrency(project: Project) {
        moneyMateRepository.addProject(project)
    }

}