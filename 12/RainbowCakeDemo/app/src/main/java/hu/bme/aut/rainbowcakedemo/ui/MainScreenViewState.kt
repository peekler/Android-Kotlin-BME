package hu.bme.aut.rainbowcakedemo.ui

sealed class MainScreenViewState

object Initial : MainScreenViewState()

object Loading : MainScreenViewState()

data class DataReady(val moneyResult: String) : MainScreenViewState()

object NetworkError: MainScreenViewState()