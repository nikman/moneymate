package com.niku.moneymate.projects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.niku.moneymate.uiutils.BaseListItem
import java.util.*

@Entity
data class Project(
    @PrimaryKey val project_id: UUID = UUID.randomUUID(),
    var project_title: String = "",
    var is_active: Boolean = true,
    val project_external_id: Int = 0
): BaseListItem {
    override fun getItemTitle(): String = project_title
}