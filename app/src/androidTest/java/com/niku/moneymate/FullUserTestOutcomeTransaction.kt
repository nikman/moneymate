package com.niku.moneymate

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FullUserTestOutcomeTransaction {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun fullUserTestOutcomeTransaction() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.currencies), withContentDescription("Currencies"),
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
                withId(R.id.new_currency), withContentDescription("New currency"),
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
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText(""))

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
        appCompatEditText3.perform(replaceText("TEST CUR"), closeSoftKeyboard())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.projects), withContentDescription("Projects"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.new_project), withContentDescription("New project"),
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

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.project_title),
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
        appCompatEditText4.perform(replaceText("test project"), closeSoftKeyboard())

        val appCompatCheckBox = onView(
            allOf(
                withId(R.id.project_isDefault), withText("Default"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatCheckBox.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.categories), withContentDescription("Categories"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val actionMenuItemView3 = onView(
            allOf(
                withId(R.id.new_category), withContentDescription("New category"),
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
        actionMenuItemView3.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.category_title),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("test category"), closeSoftKeyboard())

        val appCompatCheckBox2 = onView(
            allOf(
                withId(R.id.category_isDefault), withText("Default"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatCheckBox2.perform(click())

        val bottomNavigationItemView4 = onView(
            allOf(
                withId(R.id.accounts), withContentDescription("Accounts"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView4.perform(click())

        val actionMenuItemView4 = onView(
            allOf(
                withId(R.id.new_account), withContentDescription("New account"),
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
        actionMenuItemView4.perform(click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.account_title),
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
        textInputEditText2.perform(replaceText("test acc"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.account_note),
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
        textInputEditText3.perform(replaceText("note"), closeSoftKeyboard())

        val appCompatCheckBox3 = onView(
            allOf(
                withId(R.id.account_isDefault), withText("Default"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatCheckBox3.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.ok_button), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    12
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val bottomNavigationItemView5 = onView(
            allOf(
                withId(R.id.budget), withContentDescription("Budget"),
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
        bottomNavigationItemView5.perform(click())

        val actionMenuItemView5 = onView(
            allOf(
                withId(R.id.new_transaction), withContentDescription("Add transaction"),
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
        actionMenuItemView5.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.transaction_amount), withText("0.0"),
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
        textInputEditText4.perform(replaceText("0.01"))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.transaction_amount), withText("0.01"),
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
        textInputEditText5.perform(closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.button_currency_v2), withText("RUB"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        /*val materialTextView = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(3)
        materialTextView.perform(click())*/

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.button_project_v2),
                childAtPosition(
                    allOf(
                        withId(R.id.linear_buttons_row2),
                        childAtPosition(
                            withId(R.id.main),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        /*val materialTextView2 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(0)
        materialTextView2.perform(click())*/

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.button_category_v2),
                childAtPosition(
                    allOf(
                        withId(R.id.linear_buttons_row2),
                        childAtPosition(
                            withId(R.id.main),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        /*val materialTextView3 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(0)
        materialTextView3.perform(click())*/

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.button_project_v2),
                childAtPosition(
                    allOf(
                        withId(R.id.linear_buttons_row2),
                        childAtPosition(
                            withId(R.id.main),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        /*val materialTextView4 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(1)
        materialTextView4.perform(click())*/

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.button_category_v2),
                childAtPosition(
                    allOf(
                        withId(R.id.linear_buttons_row2),
                        childAtPosition(
                            withId(R.id.main),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        /*val materialTextView5 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(1)
        materialTextView5.perform(click())*/

        val materialButton2 = onView(
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
        materialButton2.perform(click())

        /*val textView = onView(
            allOf(
                withId(R.id.transaction_amount), withText("-0.01"),
                withParent(withParent(withId(R.id.recycler_view))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("-0.01")))

        val textView2 = onView(
            allOf(
                withId(R.id.transaction_category), withText("test category"),
                withParent(withParent(withId(R.id.recycler_view))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("test category")))

        val textView3 = onView(
            allOf(
                withId(R.id.transaction_project), withText("test project"),
                withParent(withParent(withId(R.id.recycler_view))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("test project")))*/
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
