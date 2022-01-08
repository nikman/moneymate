package com.niku.moneymate.payee

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "payee")
data class Payee (

    @NotNull
    @PrimaryKey(autoGenerate = true)
    val payee_id: Int,

    @NotNull
    var payee_name: String = ""

)