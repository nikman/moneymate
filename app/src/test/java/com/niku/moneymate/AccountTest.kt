package com.niku.moneymate

import com.niku.moneymate.account.Account
import com.niku.moneymate.utils.UUID_CURRENCY_RUB
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

private const val ACCOUNT_TITLE = "testTitle"
private const val ACCOUNT_BALANCE = 3.0

class AccountTest {

    private lateinit var subject: Account

    @Before
    fun setUp() {
        subject = Account(
            account_id = UUID.randomUUID(),
            title = ACCOUNT_TITLE,
            balance = ACCOUNT_BALANCE,
            note = "testNote",
            currency_id = UUID.fromString(UUID_CURRENCY_RUB))
    }

    @Test
    fun exposesAccountTitleAsTestTitle() {
        assertThat(subject.title, `is`(ACCOUNT_TITLE))
    }

    @Test
    fun exposesAccountBalanceAsTestBalance() {
        assertThat(subject.balance, `is`(ACCOUNT_BALANCE))
    }
}