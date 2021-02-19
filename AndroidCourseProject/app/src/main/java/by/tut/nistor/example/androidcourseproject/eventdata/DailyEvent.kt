package by.tut.nistor.example.androidcourseproject.eventdata

import java.io.Serializable

data class DailyEvent(var description: String,
                      var dailyEvents: List<Event>,
                      var repeat: Boolean = true,

                      var monday: Boolean = false,
                      var tuesday: Boolean = false,
                      var wednesday: Boolean = false,
                      var thursday: Boolean = false,
                      var friday: Boolean = false,
                      var saturday: Boolean = false,
                      var sunday: Boolean = false
) : Serializable {
}