package by.tut.nistor.example.androidcourseproject.repository.mappers

import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.database.EventEntity
import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent

interface RepositoryModelToDatabaseModelMapper {
    fun mapRepositoryModelToDatabaseModel(event: Event): EventEntity
    fun mapRepositoryDailyModelToDatabaseDailyModel(dailyEvent: DailyEvent): List<EventEntity>
}