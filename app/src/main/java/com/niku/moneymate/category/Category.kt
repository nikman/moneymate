package com.niku.moneymate.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Category (
    @PrimaryKey val category_id: UUID = UUID.randomUUID(),
    var category_type: Int = 0,
    //var parent_category_id: UUID = UUID.randomUUID(),
    var category_title: String = "",
    var is_active: Boolean = true,
    val category_external_id: Int = 0
)