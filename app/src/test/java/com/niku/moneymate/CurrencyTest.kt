package com.niku.moneymate

import com.niku.moneymate.currency.MainCurrency
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import java.util.*

private const val CURRENCY_CODE = 123
private const val CURRENCY_TITLE = "testTitle"

class CurrencyTest {

    private lateinit var subject: MainCurrency

    @Before
    fun setUp() {
        subject = MainCurrency(
            currency_id = UUID.randomUUID(),
            currency_code = CURRENCY_CODE,
            currency_title = CURRENCY_TITLE)
    }

    @Test
    fun exposesCurrencyCodeAsTestCode() {
        MatcherAssert.assertThat(subject.currency_code, CoreMatchers.`is`(CURRENCY_CODE))
    }

    @Test
    fun exposesCurrencyTitleAsTestTitle() {
        MatcherAssert.assertThat(subject.currency_title, CoreMatchers.`is`(CURRENCY_TITLE))
    }

}