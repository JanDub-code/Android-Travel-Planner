package cz.pef.TravelPlanner.databaseDi

import cz.pef.TravelPlanner.database.TravelDao
import cz.pef.TravelPlanner.database.TravelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideTravelDao(database: TravelDatabase): TravelDao {
        return database.travelDao()
    }
}