package com.niku.moneymate

import com.niku.moneymate.category.Category
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import java.util.*

private const val CATEGORY_TITLE = "testTitle"
private const val CATEGORY_TYPE = 1

class CategoryTest {

    private lateinit var subject: Category

    @Before
    fun setUp() {
        subject = Category(
            category_id = UUID.randomUUID(),
            category_title = CATEGORY_TITLE,
            category_type = CATEGORY_TYPE)
    }

    @Test
    fun exposesCurrencyCodeAsTestCode() {
        MatcherAssert.assertThat(subject.category_title, CoreMatchers.`is`(CATEGORY_TITLE))
    }

    @Test
    fun exposesCurrencyTitleAsTestTitle() {
        MatcherAssert.assertThat(subject.category_type, CoreMatchers.`is`(CATEGORY_TYPE))
    }

}