package by.tut.nistor.example.androidcourseproject.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventEntity::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun getEventDao(): EventDao
}
