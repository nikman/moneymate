package com.niku.moneymate.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.*

class MoneyMateTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = uuid?.let { UUID.fromString(uuid) }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {

        return millisSinceEpoch?.let {
            Date(it)
        }
    }

}