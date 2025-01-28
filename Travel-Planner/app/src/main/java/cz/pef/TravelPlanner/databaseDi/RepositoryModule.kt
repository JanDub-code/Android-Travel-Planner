package cz.pef.TravelPlanner.databaseDi

import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.database.TravelDao
import cz.pef.TravelPlanner.database.TravelDatabaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalTasksRepository(dao: TravelDao): ITravelRepository {
        return TravelDatabaseImpl(dao)
    }

}