package hu.bme.aut.weatherdemo.data.disk

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import hu.bme.aut.weatherdemo.data.disk.dao.CityDao
import javax.inject.Singleton

@Module
class DiskModule {
    companion object {
        private const val DB_NAME = "cities.db"
    }

    @Provides
    @Singleton
    fun provideCityDao(db: AppDatabase): CityDao = db.cityDao()

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}