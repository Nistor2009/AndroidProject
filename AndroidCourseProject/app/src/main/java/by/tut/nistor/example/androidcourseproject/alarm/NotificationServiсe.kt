package by.tut.nistor.example.androidcourseproject.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import by.tut.nistor.example.androidcourseproject.EditEventActivity
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.R
import by.tut.nistor.example.androidcourseproject.database.DbBuilder
import by.tut.nistor.example.androidcourseproject.editEvent
import by.tut.nistor.example.androidcourseproject.repository.EventRepository
import by.tut.nistor.example.androidcourseproject.repository.EventRepositoryImpl
import by.tut.nistor.example.androidcourseproject.repository.EventRepositoryListener
import java.util.*

class NotificationService : Service(), EventRepositoryListener<List<Event>> {

    private var eventList: List<Event> = Collections.emptyList()
    override fun onDestroy() {
        Log.d("LOG", "onDestroy")
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return SimpleBinder()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("LOG", "from service")
        getEventList()
        Thread {
            Thread.sleep(3000)
            Log.d("LOG","from Thread")
            startAlarm(eventList)
        }.start()
        val powerTag: String = "alarm: Single alarm"
        val powerManager: PowerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock: PowerManager.WakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, powerTag)
        wakeLock.acquire()
        val description: Event = intent.getSerializableExtra(serializableExtra) as Event
        createNotification(baseContext, description)
        showNotification(1, createNotification(baseContext, description))
        wakeLock.release()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotification(context: Context, description: Event): Notification {
        // обработка нажатия
        val intent: Intent = Intent(context, EditEventActivity::class.java)
        intent.putExtra(editEvent, description)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(description.description)
                .setContentTitle("Event time!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        return notification
    }

    private fun showNotification(id: Int, notification: Notification) {
        Log.d("LOG", "showNotification")
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(id, notification)
    }

    private fun createNotificationChannel() {
        //проверка версии
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("CHANNEL_ID", "Schedule", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    inner class SimpleBinder : Binder() {
        fun setEventList(newEventList: List<Event>) {
            eventList = newEventList
        }
    }

    override fun onEventReceive(data: List<Event>) {
        Log.d("LOG", "onEventReceive")
        eventList = data
    }

    private fun getEventList() {
        Log.d("LOG", "getEventList")
        //получить лист на сегодня из базы
        val builder: DbBuilder = DbBuilder
        builder.initDatabase(this)
        val eventRepository: EventRepository = EventRepositoryImpl(this)
        val gcalendar: GregorianCalendar = GregorianCalendar()
        val year = gcalendar.get(GregorianCalendar.YEAR)
        val month = gcalendar.get(GregorianCalendar.MONTH)
        val dayOfMonth = gcalendar.get(GregorianCalendar.DAY_OF_MONTH)
        val dayOfWeek = gcalendar.get(GregorianCalendar.DAY_OF_WEEK)
        eventRepository.getEventFromDay(dayOfMonth, month, year, dayOfWeek)
    }

    private fun startAlarm(eventListForToday: List<Event>) {
        // подключаем receiver
        val broadcastAlarm: BroadcastAlarm = BroadcastAlarm()
        broadcastAlarm.setOnetimeTimer(this.applicationContext, eventListForToday)
    }
}