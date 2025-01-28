package cz.pef.TravelPlanner.di

import cz.pef.TravelPlanner.communication.PetsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providePetsAPI(retrofit: Retrofit): PetsAPI {
        return retrofit.create(PetsAPI::class.java)
    }

}