package com.niku.moneymate.projects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Project(
    var project_title: String = "",
    @PrimaryKey val project_id: UUID = UUID.randomUUID()
)