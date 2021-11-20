package com.niku.moneymate

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*
import javax.security.auth.Subject

private const val ACCOUNT_TITLE = "testTitle"
private const val ACCOUNT_BALANCE = 3

class AccountTest {

    private lateinit var subject: Account

    @Before
    fun setUp() {
        subject = Account(UUID.randomUUID(), ACCOUNT_TITLE, ACCOUNT_BALANCE, "testNote")
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