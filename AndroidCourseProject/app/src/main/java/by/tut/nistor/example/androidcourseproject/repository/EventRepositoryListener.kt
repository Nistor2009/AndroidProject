package by.tut.nistor.example.androidcourseproject.repository

interface EventRepositoryListener<T> {
    fun onEventReceive(data: T)
}