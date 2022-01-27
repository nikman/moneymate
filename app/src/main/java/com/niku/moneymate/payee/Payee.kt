package com.niku.moneymate.payee

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "payee")
data class Payee (

    @NotNull
    @PrimaryKey(autoGenerate = true)
    val payee_id: Int = 0,

    @NotNull
    var payee_title: String = "",

    var is_active: Boolean = true,

    val payee_external_id: Int = 0

)