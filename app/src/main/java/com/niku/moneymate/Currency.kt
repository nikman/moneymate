package com.niku.moneymate

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Currency(@PrimaryKey val id: UUID = UUID.randomUUID(),
                    var code: Int = 0,
                    var title: String = "")
