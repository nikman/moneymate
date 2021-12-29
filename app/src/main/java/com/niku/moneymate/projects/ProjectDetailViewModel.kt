package com.niku.moneymate.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.niku.moneymate.account.Account
import com.niku.moneymate.database.MoneyMateRepository
import java.util.*

class ProjectDetailViewModel: ViewModel() {

    private val moneyMateRepository = MoneyMateRepository.get()
    private val projectIdLiveData = MutableLiveData<UUID>()

    var projectLiveData: LiveData<Project?> =
        Transformations.switchMap(projectIdLiveData) {
                projectId -> moneyMateRepository.getProject(projectId)
        }

    fun loadProject(projectId: UUID) {
        projectIdLiveData.value = projectId
    }

    fun saveProject(project: Project) {
        moneyMateRepository.updateProject(project)
    }

    /*fun getDefaultCurrency(): MainCurrency {
        moneyMateRepository.getDefaultCurrency()
    }*/

}