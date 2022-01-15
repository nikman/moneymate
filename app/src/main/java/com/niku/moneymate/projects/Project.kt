package com.niku.moneymate.projects

import androidx.room.Entity
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Project(
    @PrimaryKey val project_id: UUID = UUID.randomUUID(),
    var project_title: String = "",
    var is_active: Boolean = true,
    val project_external_id: Int = 0
)