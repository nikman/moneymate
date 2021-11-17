package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.niku.moneymate.Account
import java.util.*

@Dao
interface MoneyMateDao {

    @Query("SELECT * FROM account")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE id=(:id)")
    fun getAccount(id: UUID): LiveData<Account?>

    @Update
    fun updateAccount(account: Account)

    @Insert
    fun addAccount(account: Account)

}