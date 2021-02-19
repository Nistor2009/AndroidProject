package by.tut.nistor.example.androidcourseproject.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import by.tut.nistor.example.androidcourseproject.MainActivity

object DbBuilder {
    private var eventDatabase: EventDatabase? = null

    fun initDatabase(context: Context) {
        if (eventDatabase == null) {
            eventDatabase = Room.databaseBuilder(context, EventDatabase::class.java, "eventdatabase.db").build()
        }
    }

    fun getDatabase(): RoomDatabase {
        return eventDatabase as EventDatabase
    }
}