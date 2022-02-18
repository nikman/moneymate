package com.niku.moneymate


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class FullUserTestTransactionAddAndDeleteDefaultCurrency {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun fullUserTestTransactionAddAndDeleteDefaultCurrency() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.currencies),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.new_currency),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.currency_code),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("1230"))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.currency_code),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.currency_title),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard())

        val appCompatCheckBox = onView(
            allOf(
                withId(R.id.currency_isDefault),
                isDisplayed()
            )
        )
        appCompatCheckBox.perform(click())

        Espresso.pressBack()

        onView(withId(R.id.recycler_view)).perform(
            actionOnItemAtPosition<ViewHolder>(
                3, GeneralSwipeAction(
                    Swipe.SLOW,
                    GeneralLocation.BOTTOM_RIGHT,
                    GeneralLocation.BOTTOM_LEFT,
                    Press.FINGER
                )
            )
        )

        /*val materialButton = onView(
            allOf(
                withId(android.R.id.button1)
            )
        )
        materialButton.perform(scrollTo(), click())*/

        val materialButton2 = onView(
            allOf(
                withId(R.id.snackbar_action),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.nav_host_fragment),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(3, click()))

        val appCompatCheckBox2 = onView(
            allOf(
                withId(R.id.currency_isDefault),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatCheckBox2.perform(click())

        Espresso.pressBack()

        onView(withId(R.id.recycler_view)).perform(
            actionOnItemAtPosition<ViewHolder>(
                3, GeneralSwipeAction(
                    Swipe.SLOW,
                    GeneralLocation.BOTTOM_RIGHT,
                    GeneralLocation.BOTTOM_LEFT,
                    Press.FINGER
                )
            )
        )

        val materialButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("YES"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton3.perform(scrollTo(), click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.budget),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.new_transaction),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(click())

        val button = onView(
            allOf(
                withId(R.id.button_currency_v2),
                withParent(
                    allOf(
                        withId(R.id.main),
                        withParent(withId(R.id.nav_host_fragment))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton4 = onView(
            allOf(
                withId(R.id.ok_button),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.nav_host_fragment),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val textInputEditText = onView(
            allOf(
                withId(R.id.transaction_amount),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("0.01"))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.transaction_amount),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.ok_button),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.transaction_amount),
                withParent(withParent(withId(R.id.recycler_view))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("-0.01")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
