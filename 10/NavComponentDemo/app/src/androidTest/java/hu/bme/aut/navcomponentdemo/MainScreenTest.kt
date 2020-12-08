package hu.bme.aut.navcomponentdemo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @Test
    fun button_go_navigate_to_details() {
        // Given
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        val mainFragmentScenario = launchFragmentInContainer<MainFragment>()
        mainFragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // When
        Espresso.onView(withId(R.id.btnDetail)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailsFragment)
    }


    @Test
    fun button_go_provides_person_as_argument() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Create a graphical FragmentScenario for the TitleScreen
        val mainFragmentScenario = launchFragmentInContainer<MainFragment>()

        // Set the NavController property on the fragment
        mainFragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Espresso.onView(withId(R.id.btnDetail)).perform(ViewActions.click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailsFragment)
        var currentArgs = navController.backStack.last().arguments!!
        //val argPerson = currentArgs[MainFragment.KEY_PERSON] as Person
        val argPerson = currentArgs["person"] as Person

        val expectedPerson = Person("Demo Elek", "Neverland")

        assertThat(argPerson).isEqualTo(expectedPerson)
    }
}