package by.tut.nistor.example.androidcourseproject.repository

import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.database.EventEntity
import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent
import by.tut.nistor.example.androidcourseproject.repository.mappers.AllEventEntityToEventMapper
import by.tut.nistor.example.androidcourseproject.repository.mappers.EventToEventEntityMapper
import by.tut.nistor.example.androidcourseproject.repository.mappers.RepositoryModelToDatabaseModelMapper
import java.util.*

class EventRepositoryImpl(private val listener: EventRepositoryListener<List<Event>>)
    : EventRepository, EventRepositoryListener<List<EventEntity>> {

    private val repositoryDataSource: RepositoryDataSource = DatabaseSource(this)
    private val repositoryModelToDatabaseModelMapper: RepositoryModelToDatabaseModelMapper = EventToEventEntityMapper
    private var eventList: List<Event> = Collections.emptyList()
    private val entityListMapper: (List<EventEntity>) -> List<Event> = AllEventEntityToEventMapper()


    override fun getAllEvents() {
        repositoryDataSource.getAllEvents()
    }

    override fun addEvent(event: Event) {
        val eventEntity: EventEntity = repositoryModelToDatabaseModelMapper.mapRepositoryModelToDatabaseModel(event)
        repositoryDataSource.addSingleEvent(eventEntity)
    }

    override fun addDailyEvent(dailyEvent: DailyEvent) {
        val eventEntityList: List<EventEntity> = repositoryModelToDatabaseModelMapper.mapRepositoryDailyModelToDatabaseDailyModel(dailyEvent)
        eventEntityList.forEach { eventEntity ->
            repositoryDataSource.addSingleEvent(eventEntity)
        }
    }

    override fun changeEvent() {

    }

    override fun deleteEvent(event: Event) {
        val eventEntity: EventEntity = repositoryModelToDatabaseModelMapper.mapRepositoryModelToDatabaseModel(event)
        repositoryDataSource.deleteEvent(eventEntity)
    }

    override fun getEventFromDay(day: Int, month: Int, year: Int, dayOfWeek: Int) {
        repositoryDataSource.getEventFromDay(day, month, year, dayOfWeek)

    }

    override fun getDailyEvents(dayOfWeek: Int) {
        repositoryDataSource.getDailyEvent(dayOfWeek)
    }

    override fun getAllDailyEvents() {
        repositoryDataSource.getAllDailyEvent()
    }

    override fun onEventReceive(data: List<EventEntity>) {
        eventList = entityListMapper(data)
        val sortedList: MutableList<Event> = eventList.toMutableList()
        sortedList.sortWith(compareBy({ it.year }, { it.month }, { it.dayOfMonth }, { it.hour }, { it.minute }))
        eventList = sortedList
        listener.onEventReceive(eventList)
    }

}