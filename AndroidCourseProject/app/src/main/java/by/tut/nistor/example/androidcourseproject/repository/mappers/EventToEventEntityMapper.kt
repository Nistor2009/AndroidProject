package by.tut.nistor.example.androidcourseproject.repository.mappers

import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.database.EventEntity
import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent

object EventToEventEntityMapper : RepositoryModelToDatabaseModelMapper {
    override fun mapRepositoryModelToDatabaseModel(event: Event): EventEntity {
        val eventEntity: EventEntity = EventEntity()
        return eventEntity.apply {
            description = event.description
            year = event.year
            month = event.month
            dayOfMonth = event.dayOfMonth
            dayOfWeek = event.dayOfWeek
            hour = event.hour
            minute = event.minute
            repeat = event.repeat
            isDone = event.isDone
            isCancelled = event.isCancelled
            isDeleted = event.isDeleted
        }
    }

    override fun mapRepositoryDailyModelToDatabaseDailyModel(dailyEvent: DailyEvent): List<EventEntity> {
        val eventList: MutableList<EventEntity> = mutableListOf()
        eventList.addAll(dailyEvent.dailyEvents.map { event -> mapRepositoryModelToDatabaseModel(event) })
        return eventList.toList()
    }
}

