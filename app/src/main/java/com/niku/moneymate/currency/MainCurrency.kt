package com.niku.moneymate.currency

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MainCurrency(
    @PrimaryKey val currency_id: UUID = UUID.randomUUID(),
    var currency_code: Int = 0,
    var currency_title: String = ""//,
    //var currency_is_default: Boolean = false
)
