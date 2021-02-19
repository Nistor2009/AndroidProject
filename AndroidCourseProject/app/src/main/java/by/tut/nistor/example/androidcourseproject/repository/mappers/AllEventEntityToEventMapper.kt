package by.tut.nistor.example.androidcourseproject.repository.mappers

import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.database.EventEntity

class AllEventEntityToEventMapper : (List<EventEntity>) -> List<Event> {
    override fun invoke(eventEntityList: List<EventEntity>): List<Event> =
            eventEntityList.map { item ->
                Event(
                        description = item.description,
                        year = item.year,
                        month = item.month,
                        dayOfMonth = item.dayOfMonth,
                        dayOfWeek = item.dayOfWeek,
                        hour = item.hour,
                        minute = item.minute,
                        repeat = item.repeat,
                        isCancelled = item.isCancelled,
                        isDeleted = item.isDeleted,
                        isDone = item.isDone
                )
            }
}