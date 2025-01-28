package cz.pef.TravelPlanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.pef.TravelPlanner.models.InterestEntity
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings


@Database(entities = [UserSettings::class,InterestEntity::class,PlaceResultEntity::class,SavedPlaceEntity::class], version = 8, exportSchema = true)
abstract class TravelDatabase : RoomDatabase() {

    abstract fun travelDao(): TravelDao

    companion object {
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            if (INSTANCE == null) {
                synchronized(TravelDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TravelDatabase::class.java, "travel_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}