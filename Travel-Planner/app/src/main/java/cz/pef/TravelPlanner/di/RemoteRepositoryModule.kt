package cz.pef.TravelPlanner.di


import cz.pef.TravelPlanner.communication.IPetsRemoteRepository
import cz.pef.TravelPlanner.communication.PetsAPI
import cz.pef.TravelPlanner.communication.PetsRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun providePetsRepository(petsAPI: PetsAPI): IPetsRemoteRepository {
        return PetsRemoteRepositoryImpl(petsAPI)
    }
}
