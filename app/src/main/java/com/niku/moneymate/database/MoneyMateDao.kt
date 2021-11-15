package com.niku.moneymate.database

import androidx.room.Dao
import androidx.room.Query
import com.niku.moneymate.Account
import java.util.*

@Dao
interface MoneyMateDao {

    @Query("SELECT * FROM account")
    fun getAccounts(): List<Account>

    @Query("SELECT * FROM account WHERE id=(:id)")
    fun getAccount(id: UUID): Account?

}