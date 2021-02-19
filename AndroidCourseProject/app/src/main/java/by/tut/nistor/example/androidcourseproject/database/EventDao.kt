package by.tut.nistor.example.androidcourseproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import io.reactivex.Single

@Dao
interface EventDao {

    @Insert(onConflict = ABORT)
    fun insert(eventEntity: EventEntity): Single<Long>

    @Query("SELECT * FROM Events WHERE repeat = 0")
    fun getAllEvents(): Single<List<EventEntity>>

    @Query("SELECT * FROM Events WHERE dayOfMonth = :day AND month = :month AND year = :year")
    fun getEventFromDay(day: Int, month: Int, year: Int): Single<List<EventEntity>>

    @Query("SELECT * FROM Events WHERE dayOfWeek = :dayOfWeek AND repeat = 1")
    fun getDailyEvent(dayOfWeek: Int): Single<List<EventEntity>>

    @Query("SELECT * FROM Events WHERE repeat = 1")
    fun getAllDailyEvent(): Single<List<EventEntity>>

    @Query("DELETE FROM Events WHERE description = :description AND year = :year AND month = :month AND dayOfMonth = :dayOfMonth AND hour = :hour AND minute = :minute")
    fun delete(description: String, year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int): Single<Int>

}
