package by.tut.nistor.example.androidcourseproject.repository

import by.tut.nistor.example.androidcourseproject.database.EventEntity

interface RepositoryDataSource {
    fun addSingleEvent(event: EventEntity)
    fun deleteEvent(event: EventEntity)
    fun getAllEvents()
    fun getEventFromDay(day: Int, month: Int, year: Int, dayOfWeek: Int)
    fun getDailyEvent(dayOfWeek: Int)
    fun getAllDailyEvent()
}