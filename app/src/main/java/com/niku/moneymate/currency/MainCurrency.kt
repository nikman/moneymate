package com.niku.moneymate.currency

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MainCurrency(@PrimaryKey val id: UUID = UUID.randomUUID(),
                        var currency_code: Int = 0,
                        var title: String = "")
