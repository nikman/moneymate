package com.niku.moneymate

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.After
import org.junit.Test

class CurrencyFragmentInstrumentedTest {

    lateinit var scenario: ActivityScenario<MainActivity>

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun whenStartingActivity_thenOpeningCurrencyList() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.currencies))
            .perform(ViewActions.click())

    }

    @Test
    fun whenStartingActivity_thenOpeningProjectsList() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.projects))
            .perform(ViewActions.click())

    }

    @Test
    fun whenStartingActivity_thenOpeningCategoriesList() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.categories))
            .perform(ViewActions.click())

    }

    @Test
    fun whenStartingActivity_thenOpeningAccountsList() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.accounts))
            .perform(ViewActions.click())

    }

    @Test
    fun whenStartingActivity_thenOpeningTransactionsList() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.budget))
            .perform(ViewActions.click())

    }

    @Test
    fun whenStartingActivity_thenOpeningCurrencyList_thenCreatingNewCurrency() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.currencies))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.new_currency))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.currency_code))
            .perform(ViewActions.click())
            .perform(ViewActions.typeText("678"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.currency_title))
            .perform(ViewActions.click())
            .perform(ViewActions.typeText("TEST"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.pressBack()

    }

    @Test
    fun whenStartingActivity_thenOpeningCurrencyList_thenCreatingNewDefaultCurrency() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.currencies))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.new_currency))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.currency_code))
            .perform(ViewActions.click())
            .perform(ViewActions.typeText("678"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.currency_title))
            .perform(ViewActions.click())
            .perform(ViewActions.typeText("TEST default"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.currency_isDefault))
            .perform(ViewActions.click())

        Espresso.pressBack()

    }

    @Test
    fun whenStartingActivity_thenOpeningProjectsList_thenCreatingNewProject() {

        scenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.projects))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.new_project))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.project_title))
            .perform(ViewActions.click())
            .perform(ViewActions.typeText("TEST project"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.pressBack()

    }

}