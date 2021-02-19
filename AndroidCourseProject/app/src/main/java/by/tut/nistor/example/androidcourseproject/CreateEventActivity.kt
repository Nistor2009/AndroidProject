package by.tut.nistor.example.androidcourseproject

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import java.util.*


class CreateEventActivity : AppCompatActivity() {
    private val calendar: Calendar = Calendar.getInstance()
    private val year: Int = calendar.get(Calendar.YEAR)
    private val month: Int = calendar.get(Calendar.MONTH)
    private val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
    private val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
    private lateinit var eventTime: TextView
    private lateinit var eventDate: TextView
    private var event: Event = Event(description = "",
            year = year,
            month = month + 1,
            dayOfMonth = dayOfMonth,
            dayOfWeek = dayOfWeek,
            hour = 0,
            minute = 0,
            repeat = false,
            isDone = false,
            isDeleted = false,
            isCancelled = false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val editDescription: EditText = findViewById(R.id.viewEditDescription)
        val viewButtonNext: Button = findViewById(R.id.viewButtonSaveEvent)
        eventDate = findViewById(R.id.viewTextEventDate)
        eventTime = findViewById(R.id.viewTextEventTime)

        viewButtonNext.setOnClickListener {
            val text: String = editDescription.text.toString()
            if (text != "") {
                event.description = text
                if (eventTime.text != "Event time") {
                    val intent: Intent = Intent()
                    intent.putExtra("Created event", event)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Add event time", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Add event description", Toast.LENGTH_SHORT).show()
            }
        }

        // дата события
        eventDate.setOnClickListener(View.OnClickListener {
            val dialog: DatePickerDialog = DatePickerDialog(this,
                    R.style.Theme_AppCompat_Light_Dialog,
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                        val month = month + 1
                        event.year = year
                        event.month = month
                        event.dayOfMonth = day
                        val gregorianCalendar = GregorianCalendar(year, month, day - 1)
                        val dayOfWeek: Int = gregorianCalendar.get(Calendar.DAY_OF_WEEK)
                        event.dayOfWeek = dayOfWeek
                        eventDate.text = "${event.dayOfMonth}.${event.month}.${event.year}"
                    },
                    year,
                    month,
                    dayOfMonth)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            dialog.show()
        })

        // время события
        eventTime.setOnClickListener {
            val timeDialog: TimePickerDialog = TimePickerDialog(this,
                    TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                        event.hour = hour
                        event.minute = minute
                        calendar.set(0, 0, 0, event.hour, event.minute)
                        val textTime: String = (DateFormat.format("HH:mm", calendar)).toString()
                        eventTime.text = textTime
                    },
                    12,
                    0,
                    true)
            timeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            timeDialog.updateTime(event.hour, event.minute)
            timeDialog.show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("event", event)
        outState.putString("textDate", eventDate.text.toString())
        outState.putString("textTime", eventTime.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val event: Event = savedInstanceState.getSerializable("event") as Event
        this.event = event
        eventDate.text = savedInstanceState.getString("textDate")
        eventTime.text = savedInstanceState.getString("textTime")

    }

}