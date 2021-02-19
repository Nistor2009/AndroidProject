package by.tut.nistor.example.androidcourseproject.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import java.lang.Boolean
import java.util.*

const val powerTag: String = "alarm: Single alarm"
const val serializableExtra = "DESCRIPTION"

class BroadcastAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent: Intent = Intent(context, NotificationService::class.java)
        val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock: PowerManager.WakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, powerTag)
        wakeLock.acquire()
        val description: Event = Event(description = intent.getStringExtra("description") as String,
                year = intent.getIntExtra("year", 1),
                month = intent.getIntExtra("month", 1),
                dayOfMonth = intent.getIntExtra("dayOfMonth", 1),
                dayOfWeek = intent.getIntExtra("dayOfWeek", 1),
                hour = intent.getIntExtra("hour", 1),
                minute = intent.getIntExtra("minute", 1),
                repeat = intent.getBooleanExtra("repeat", false)
        )
        serviceIntent.putExtra(serializableExtra, description)
        context.startService(serviceIntent)
        wakeLock.release()
    }

    fun setAlarm(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BroadcastAlarm::class.java)
        intent.putExtra(powerTag, Boolean.FALSE)
        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        //After after 5 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5.toLong(), pi)
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, BroadcastAlarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun setOnetimeTimer(context: Context, eventListForToday: List<Event>) {
        val sortedList: MutableList<Event> = eventListForToday.toMutableList()
        sortedList.sortWith(compareBy({ it.hour }, { it.minute }))
        Log.d("LOG", sortedList.toString())
        if (sortedList.isNotEmpty()) {
            val gcalendar: GregorianCalendar = GregorianCalendar()
            val year = gcalendar.get(GregorianCalendar.YEAR)
            val month = gcalendar.get(GregorianCalendar.MONTH)
            val dayOfMonth = gcalendar.get(GregorianCalendar.DAY_OF_MONTH)
            var futureCalendar: GregorianCalendar
            var event: Event
            var i: Int = 0
            do {
                event = sortedList[i]
                futureCalendar = GregorianCalendar(year, month, dayOfMonth, event.hour, event.minute, 0)
                i++
            } while (i < sortedList.size &&
                    futureCalendar <= gcalendar)
            if (futureCalendar <= gcalendar) {
                futureCalendar = GregorianCalendar(year, month, dayOfMonth + 1, 0, 1, 0)
                event = Event(description = "Проверка задач на новый день",
                        year = futureCalendar.get(GregorianCalendar.YEAR),
                        month = futureCalendar.get(GregorianCalendar.MONTH),
                        dayOfMonth = futureCalendar.get(GregorianCalendar.DAY_OF_MONTH),
                        dayOfWeek = futureCalendar.get(GregorianCalendar.DAY_OF_WEEK),
                        hour = futureCalendar.get(GregorianCalendar.HOUR_OF_DAY),
                        minute = futureCalendar.get(GregorianCalendar.MINUTE))
            }
            Log.d("LOG", event.toString())
            val alarmManager: AlarmManager = context.getSystemService(android.app.Service.ALARM_SERVICE) as android.app.AlarmManager
            val intent22 = Intent(context, BroadcastAlarm::class.java)
            intent22.putExtra("description", event.description)
            intent22.putExtra("year", event.year)
            intent22.putExtra("month", event.month)
            intent22.putExtra("dayOfMonth", event.dayOfMonth)
            intent22.putExtra("dayOfWeek", event.dayOfWeek)
            intent22.putExtra("hour", event.hour)
            intent22.putExtra("minute", event.minute)
            intent22.putExtra("repeat", event.repeat)
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 100, intent22, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        futureCalendar.timeInMillis, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureCalendar.timeInMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureCalendar.timeInMillis, pendingIntent);
            }
        }
    }
}
