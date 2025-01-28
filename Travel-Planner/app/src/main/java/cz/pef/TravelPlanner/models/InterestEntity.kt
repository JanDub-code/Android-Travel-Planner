package cz.pef.TravelPlanner.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interests")
data class InterestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)