package hu.bme.aut.weatherdemo.robot

import android.app.Activity
import android.text.InputType
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import hu.bme.aut.weatherdemo.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import hu.bme.aut.weatherdemo.util.withRecyclerView
import hu.bme.aut.weatherdemo.EspressoTest
import org.hamcrest.Matchers.not

inline fun <reified R : ScreenRobot<R>> EspressoTest<*>.withRobot(): R {
    return R::class.constructors.elementAt(0).call(this)
}

abstract class ScreenRobot<T : ScreenRobot<T>>(protected val espressoTest: EspressoTest<*>) {

    private var activityContext: Activity? = null

    protected fun checkIsDisplayed(@IdRes vararg viewIds: Int) = applyActions {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(isDisplayed()))
        }
    }

    protected fun checkIsHidden(@IdRes vararg viewIds: Int) = applyActions {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(not(isDisplayed())))
        }
    }

    protected fun checkViewHasText(@IdRes viewId: Int, expected: String) = applyActions {
        onView(withId(viewId)).check(matches(withText(expected)))
    }

    protected fun checkViewHasText(@IdRes viewId: Int, @StringRes messageResId: Int) =
        applyActions {
            onView(withId(viewId)).check(matches(withText(messageResId)))
        }

    protected fun checkViewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int) =
        applyActions {
            onView(withId(viewId)).check(matches(withHint(messageResId)))
        }

    protected fun clickOkOnView(@IdRes viewId: Int) = applyActions {
        onView(withId(viewId)).perform(click())
    }

    protected fun clickOkOnViewWithText(text: String) = applyActions {
        onView(withText(text)).perform(click())
    }

    protected fun enterTextIntoView(
        @IdRes viewId: Int,
        text: String,
        closeKeyboard: Boolean = false
    ) = applyActions {
        if (closeKeyboard) {
            onView(withId(viewId)).perform(typeText(text), closeSoftKeyboard())
        } else {
            onView(withId(viewId)).perform(typeText(text))
        }
    }

    protected fun enterTextIntoViewWithInputTypeText(
        text: String,
        closeKeyboard: Boolean = false
    ) = applyActions {
        if (closeKeyboard) {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText(text), closeSoftKeyboard())
        } else {
            onView(withInputType(InputType.TYPE_CLASS_TEXT)).perform(typeText(text))
        }
    }

    protected fun provideActivityContext(activityContext: Activity) = applyActions {
        this.activityContext = activityContext
    }

    protected fun checkDialogWithTextIsDisplayed(@StringRes messageResId: Int) = applyActions {
        onView(withText(messageResId))
            .inRoot(withDecorView(not(activityContext?.window?.decorView)))
            .check(matches(isDisplayed()))
    }

    protected fun checkDialogWithTextIsDisplayed(message: String) = applyActions {
        onView(withSubstring(message))
            .inRoot(withDecorView(not(activityContext?.window?.decorView)))
            .check(matches(isDisplayed()))
    }

    protected fun checkSnackbarWithTextIsDisplayed(@StringRes messageResId: Int) = applyActions {
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(messageResId)))
    }

    protected fun checkRecyclerViewItemCount(@IdRes viewId: Int, expectedItemCount: Int) =
        applyActions {
            onView(withId(viewId)).check(withItemCount(expectedItemCount))
        }

    protected fun checkRecyclerViewItemHasText(
        @IdRes viewId: Int,
        itemPosition: Int,
        @StringRes textResId: Int
    ) =
        applyActions {
            onView(withRecyclerView(viewId).atPosition(itemPosition)).check(
                matches(
                    hasDescendant(
                        withText(textResId)
                    )
                )
            )
        }

    protected fun checkRecyclerViewItemHasText(
        @IdRes viewId: Int, itemPosition: Int,
        text: String
    ) = applyActions {
        onView(withRecyclerView(viewId).atPosition(itemPosition)).check(
            matches(
                hasDescendant(
                    withText(text)
                )
            )
        )
    }

    protected fun scrollRecyclerViewToItem(@IdRes viewId: Int, position: Int) = applyActions {
        onView(withId(viewId)).perform(
            scrollToPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                position
            )
        )
    }

    protected fun clickRecyclerViewItem(@IdRes viewId: Int, position: Int) = applyActions {
        onView(withId(viewId)).perform(
            scrollToPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                position
            )
        )
        onView(withRecyclerView(viewId).atPosition(position)).perform(click())
    }

    protected fun clickViewInRecyclerViewItem(
        @IdRes viewId: Int,
        itemPosition: Int,
        @IdRes viewInItemId: Int
    ) =
        applyActions {
            onView(withRecyclerView(viewId).atPositionOnView(itemPosition, viewInItemId)).perform(
                click()
            )
        }

    protected fun swipeLeftOnView(@IdRes viewId: Int) = applyActions {
        onView(withId(viewId)).perform(swipeLeft())
    }

    protected fun swipeRightOnView(@IdRes viewId: Int) = applyActions {
        onView(withId(viewId)).perform(swipeRight())
    }

    protected fun performSwipeDownOnView(@IdRes viewId: Int) = applyActions {
        onView(withId(viewId)).perform(swipeDown())
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun applyActions(block: () -> Unit): T {
        block()
        return this as T
    }

}