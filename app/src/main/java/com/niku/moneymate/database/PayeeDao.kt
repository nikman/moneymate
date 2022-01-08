package com.niku.moneymate.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.niku.moneymate.account.Account
import com.niku.moneymate.accountWithCurrency.AccountWithCurrency
import com.niku.moneymate.payee.Payee
import java.util.*

@Dao
interface PayeeDao {

    @Transaction
    @Query("SELECT * FROM payee")
    fun getAccounts(): LiveData<List<Payee>>

    @Transaction
    @Query("SELECT * FROM payee WHERE payee_id=(:payee_id)")
    fun getAccount(payee_id: UUID): LiveData<Payee?>

    @Update
    fun updatePayee(payee: Payee)

    @Insert
    fun addPayee(payee: Payee)

}