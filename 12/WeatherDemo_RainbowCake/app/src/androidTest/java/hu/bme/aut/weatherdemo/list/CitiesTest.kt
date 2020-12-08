package hu.bme.aut.weatherdemo.list

import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.weatherdemo.AppTestComponent
import hu.bme.aut.weatherdemo.EspressoTest
import hu.bme.aut.weatherdemo.MainActivity
import hu.bme.aut.weatherdemo.robot.withRobot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CitiesTest : EspressoTest<MainActivity>(MainActivity::class.java) {

    override fun injectDependencies(injector: AppTestComponent) {
        injector.inject(this)
    }

    @Test
    fun should_ShowScrollingScreen_When_ApplicationLaunches() {
        withRobot<CitiesTestRobot>()
            .checkListVisible()
            .checkFabVisible()
            .checkToolbarVisible()
    }

    @Test
    fun should_ShowAddCityDialog_When_FabClicked() {
        withRobot<CitiesTestRobot>()
            .clickOnFab()
            .checkAddCityDialogVisible()
    }

    @Test
    fun should_ShowNewItem_When_NewCityIsAdded() {
        withRobot<CitiesTestRobot>()
            .clickOnFab()
            .addCity()
            .checkCityItemVisible()
    }

}