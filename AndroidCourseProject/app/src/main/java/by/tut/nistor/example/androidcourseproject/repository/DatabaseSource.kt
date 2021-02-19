package by.tut.nistor.example.androidcourseproject.repository

import android.util.Log
import by.tut.nistor.example.androidcourseproject.database.DbBuilder
import by.tut.nistor.example.androidcourseproject.database.EventDao
import by.tut.nistor.example.androidcourseproject.database.EventDatabase
import by.tut.nistor.example.androidcourseproject.database.EventEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Collections

class DatabaseSource(private val listener: EventRepositoryListener<List<EventEntity>>) : RepositoryDataSource {
    private val database: EventDatabase = DbBuilder.getDatabase() as EventDatabase
    private val eventDao: EventDao = database.getEventDao()
    private var eventEntityList: List<EventEntity> = Collections.emptyList()


    override fun addSingleEvent(event: EventEntity) {

        eventDao.insert(event).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("LOG", "Insert")
        },
                {

                })

    }

    override fun deleteEvent(event: EventEntity) {
        eventDao.delete(event.description, event.year, event.month, event.dayOfMonth, event.hour, event.minute).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("LOG", "Delete ${event.description}, elements: $it")
        },
                {

                })
    }

    override fun getAllEvents() {
        eventDao.getAllEvents().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ eventList ->
            eventEntityList = eventList
            listener.onEventReceive(eventEntityList)
            Log.d("LOG", "getAll")
        },
                {
                    Log.d("ERROR", "Unsuccessful data receive")
                })

    }

    override fun getDailyEvent(dayOfWeek: Int) {
        eventDao.getDailyEvent(dayOfWeek).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ eventList ->
            eventEntityList = eventList
            listener.onEventReceive(eventEntityList)
            Log.d("LOG", "getDailyEvent")
        },
                {
                    Log.d("ERROR", "Unsuccessful data receive")
                })
    }

    override fun getAllDailyEvent() {
        eventDao.getAllDailyEvent().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ eventList ->
            eventEntityList = eventList
            listener.onEventReceive(eventEntityList)
            Log.d("LOG", "getDailyEvent")
        },
                {
                    Log.d("ERROR", "Unsuccessful data receive")
                })
    }

    override fun getEventFromDay(day: Int, month: Int, year: Int, dayOfWeek: Int) {
        eventDao.getDailyEvent(dayOfWeek).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ eventList ->
            eventEntityList = eventList
        },
                {
                    Log.d("ERROR", "Unsuccessful data receive")
                })
        Thread.sleep(50)
        eventDao.getEventFromDay(day, month, year).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ eventList ->
            val allEventList: MutableList<EventEntity> = mutableListOf()
            allEventList.addAll(eventList)
            allEventList.addAll(eventEntityList)
            eventEntityList = allEventList
            listener.onEventReceive(eventEntityList)
            Log.d("LOG", "getEventFromDay")
        },
                {
                    Log.d("ERROR", "Unsuccessful data receive")
                })
    }


}