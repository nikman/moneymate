package com.niku.moneymate

import java.util.*

data class Account(val id: UUID = UUID.randomUUID(),
                   var title: String = "",
                   var balance: Int = 0)