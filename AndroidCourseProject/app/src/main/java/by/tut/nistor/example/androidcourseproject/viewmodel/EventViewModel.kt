package by.tut.nistor.example.androidcourseproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.repository.EventRepository
import by.tut.nistor.example.androidcourseproject.repository.EventRepositoryImpl
import by.tut.nistor.example.androidcourseproject.repository.EventRepositoryListener
import java.util.*

class EventViewModel : ViewModel(), EventRepositoryListener<List<Event>> {
    private val eventRepository: EventRepository = EventRepositoryImpl(this)
    private val mutableData = MutableLiveData<List<Event>>()
    val liveData: LiveData<List<Event>> = mutableData
    fun getAllEvents() {
        eventRepository.getAllEvents()
    }

    fun getEventFromToday() {
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val year: Int = calendar.get(Calendar.YEAR)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
        eventRepository.getEventFromDay(day, month, year, dayOfWeek)
    }

    fun getDailyEvents(){
        val calendar: Calendar = Calendar.getInstance()
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
        eventRepository.getDailyEvents(dayOfWeek)
    }

    fun getAllDailyEvents(){
        eventRepository.getAllDailyEvents()
    }

    fun addEvent(event: Event) {
        eventRepository.addEvent(event)
    }

    fun addDailyEvent(dailyEvent: DailyEvent){
        eventRepository.addDailyEvent(dailyEvent)
    }

    fun deleteEvent(event: Event) {
        eventRepository.deleteEvent(event)
    }

    override fun onEventReceive(data: List<Event>) {
        mutableData.value = data
    }
}