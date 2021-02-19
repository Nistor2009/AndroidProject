package by.tut.nistor.example.androidcourseproject.repository

import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent
import by.tut.nistor.example.androidcourseproject.eventdata.Event

interface EventRepository {
    fun getAllEvents()
    fun addEvent(event: Event)
    fun addDailyEvent(dailyEvent: DailyEvent)
    fun changeEvent()
    fun deleteEvent(event: Event)
    fun getEventFromDay(day: Int, month: Int, year: Int, dayOfWeek: Int)
    fun getDailyEvents(dayOfWeek: Int)
    fun getAllDailyEvents()
}