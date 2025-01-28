package cz.pef.TravelPlanner.databaseDi

import android.content.Context
import cz.pef.TravelPlanner.database.TravelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TravelDatabase {
        return TravelDatabase.getDatabase(context)
    }
}