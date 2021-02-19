package by.tut.nistor.example.androidcourseproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Events")
class EventEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var description: String = ""
    var year: Int = 1
    var month: Int = 1
    var dayOfMonth: Int = 1
    var dayOfWeek: Int = 1
    var hour: Int = 1
    var minute: Int = 1
    var repeat: Boolean = false
    var isDone: Boolean = false
    var isDeleted: Boolean = false
    var isCancelled: Boolean = false
}

