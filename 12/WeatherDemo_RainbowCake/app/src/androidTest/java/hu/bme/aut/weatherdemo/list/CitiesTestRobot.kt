package hu.bme.aut.weatherdemo.list

import hu.bme.aut.weatherdemo.EspressoTest
import hu.bme.aut.weatherdemo.R
import hu.bme.aut.weatherdemo.robot.ScreenRobot

class CitiesTestRobot(espressoTest: EspressoTest<*>) : ScreenRobot<CitiesTestRobot>(espressoTest) {

    fun checkListVisible(): CitiesTestRobot {
        return checkIsDisplayed(R.id.listCities)
    }

    fun checkFabVisible(): CitiesTestRobot {
        return checkIsDisplayed(R.id.fab)
    }

    fun checkToolbarVisible(): CitiesTestRobot {
        return checkIsDisplayed(R.id.toolbar)
    }

    fun clickOnFab() : CitiesTestRobot {
        return clickOkOnView(R.id.fab)
    }

    fun checkAddCityDialogVisible(): CitiesTestRobot {
        return checkDialogWithTextIsDisplayed("Add City Name")
    }

    fun addCity(): CitiesTestRobot {
        return enterTextIntoViewWithInputTypeText("Budapest", true)
            .clickOkOnViewWithText("Add")
    }

    fun checkCityItemVisible(): CitiesTestRobot {
        return checkRecyclerViewItemHasText(R.id.listCities, 0, "Budapest")
    }
}