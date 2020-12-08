package hu.bme.aut.rainbowcakedemo.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.rainbowcakedemo.ui.MainScreenViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    abstract fun bindUserViewModel(userViewModel: MainScreenViewModel): ViewModel
}