package com.niku.moneymate

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Account(@PrimaryKey val id: UUID = UUID.randomUUID(),
                   var title: String = "",
                   var balance: Int = 0,
                   var note: String = ""
                   //var currency: Currency? = null
)