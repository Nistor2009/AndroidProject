package by.tut.nistor.example.androidcourseproject.eventdata

import java.io.Serializable


data class Event(var description: String,
                 var year: Int,
                 var month: Int,
                 var dayOfMonth: Int,
                 var dayOfWeek: Int,
                 var hour: Int,
                 var minute: Int,
                 var repeat: Boolean = false,
                 var isDone: Boolean = false,
                 var isDeleted: Boolean = false,
                 var isCancelled: Boolean = false) : Serializable {


}