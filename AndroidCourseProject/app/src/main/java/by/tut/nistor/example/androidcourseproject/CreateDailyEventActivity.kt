package by.tut.nistor.example.androidcourseproject

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import java.util.Collections
import java.util.Calendar


class CreateDailyEventActivity : AppCompatActivity() {
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private lateinit var dailyEvent: DailyEvent
    private var isCreateEventSuccessful = false

    //кнопки дней недели и времени
    private lateinit var buttonMonday: CheckBox
    private lateinit var buttonTuesday: CheckBox
    private lateinit var buttonWednesday: CheckBox
    private lateinit var buttonThursday: CheckBox
    private lateinit var buttonFriday: CheckBox
    private lateinit var buttonSaturday: CheckBox
    private lateinit var buttonSunday: CheckBox
    private lateinit var mondayTime: TextView
    private lateinit var tuesdayTime: TextView
    private lateinit var wednesdayTime: TextView
    private lateinit var thursdayTime: TextView
    private lateinit var fridayTime: TextView
    private lateinit var saturdayTime: TextView
    private lateinit var sundayTime: TextView

    private lateinit var daysAndTime: MutableMap<CheckBox, Calendar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_create_event)
        val editDailyEventDescription: EditText = findViewById(R.id.viewEditDailyEventDescription)
        val buttonSaveDailyEvent: Button = findViewById(R.id.viewButtonSaveDailyEvent)

        //инициализируем event
        dailyEvent = DailyEvent(
                description = "",
                repeat = true,
                dailyEvents = Collections.emptyList(),
                monday = false,
                tuesday = false,
                wednesday = false,
                thursday = false,
                friday = false,
                saturday = false,
                sunday = false)

        //Инициализируем кнопки дней недели и времени
        buttonMonday = findViewById(R.id.buttonMonday)
        buttonTuesday = findViewById(R.id.buttonTuesday)
        buttonWednesday = findViewById(R.id.buttonWednesday)
        buttonThursday = findViewById(R.id.buttonThursday)
        buttonFriday = findViewById(R.id.buttonFriday)
        buttonSaturday = findViewById(R.id.buttonSaturday)
        buttonSunday = findViewById(R.id.buttonSunday)
        mondayTime = findViewById(R.id.viewMondayTime)
        tuesdayTime = findViewById(R.id.viewTuesdayTime)
        wednesdayTime = findViewById(R.id.viewWednesdayTime)
        thursdayTime = findViewById(R.id.viewThursdayTime)
        fridayTime = findViewById(R.id.viewFridayTime)
        saturdayTime = findViewById(R.id.viewSaturdayTime)
        sundayTime = findViewById(R.id.viewSundayTime)

        daysAndTime = mutableMapOf()
        daysAndTime[buttonMonday] = Calendar.getInstance()
        daysAndTime[buttonTuesday] = Calendar.getInstance()
        daysAndTime[buttonWednesday] = Calendar.getInstance()
        daysAndTime[buttonThursday] = Calendar.getInstance()
        daysAndTime[buttonFriday] = Calendar.getInstance()
        daysAndTime[buttonSaturday] = Calendar.getInstance()
        daysAndTime[buttonSunday] = Calendar.getInstance()

        // listener для времени. Работает только с нажатым флажком
        mondayTime.setOnClickListener(View.OnClickListener { if (buttonMonday.isChecked) timeOfEvent(mondayTime) })
        tuesdayTime.setOnClickListener(View.OnClickListener { if (buttonTuesday.isChecked) timeOfEvent(tuesdayTime) })
        wednesdayTime.setOnClickListener(View.OnClickListener { if (buttonWednesday.isChecked) timeOfEvent(wednesdayTime) })
        thursdayTime.setOnClickListener(View.OnClickListener { if (buttonThursday.isChecked) timeOfEvent(thursdayTime) })
        fridayTime.setOnClickListener(View.OnClickListener { if (buttonFriday.isChecked) timeOfEvent(fridayTime) })
        saturdayTime.setOnClickListener(View.OnClickListener { if (buttonSaturday.isChecked) timeOfEvent(saturdayTime) })
        sundayTime.setOnClickListener(View.OnClickListener { if (buttonSunday.isChecked) timeOfEvent(sundayTime) })

        //заполнение event данными
        buttonSaveDailyEvent.setOnClickListener {
            val text: String = editDailyEventDescription.text.toString()
            if (text == "") {
                Toast.makeText(this, "Add event description", Toast.LENGTH_SHORT).show()
            } else {
                dailyEvent.description = text
                var isDayChecked: Boolean = false
                //проходимся по всем дням. Если день выбран вызываем addEvent, который вернет результат добавления
                daysAndTime.forEach { (days, time) ->
                    if (days.isChecked) {
                        isDayChecked = true
                        addEvent(days, time, text)
                    }
                }
                if (!isDayChecked) {
                    Toast.makeText(this, "Add event day", Toast.LENGTH_SHORT).show()
                } else {
                    if (isCreateEventSuccessful) {
                        val intent: Intent = Intent()
                        intent.putExtra("Created daily event", dailyEvent)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("event", dailyEvent)
        outState.putString("mondayTime", mondayTime.text.toString())
        outState.putString("tuesdayTime", tuesdayTime.text.toString())
        outState.putString("wednesdayTime", wednesdayTime.text.toString())
        outState.putString("thursdayTime", thursdayTime.text.toString())
        outState.putString("fridayTime", fridayTime.text.toString())
        outState.putString("saturdayTime", saturdayTime.text.toString())
        outState.putString("sundayTime", sundayTime.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val dailyEvent: DailyEvent = savedInstanceState.getSerializable("event") as DailyEvent
        this.dailyEvent = dailyEvent
        mondayTime.text = savedInstanceState.getString("mondayTime")
        tuesdayTime.text = savedInstanceState.getString("tuesdayTime")
        wednesdayTime.text = savedInstanceState.getString("wednesdayTime")
        thursdayTime.text = savedInstanceState.getString("thursdayTime")
        fridayTime.text = savedInstanceState.getString("fridayTime")
        saturdayTime.text = savedInstanceState.getString("saturdayTime")
        sundayTime.text = savedInstanceState.getString("sundayTime")
    }

    // время события
    private fun timeOfEvent(view: TextView) {
        val time: Calendar = Calendar.getInstance()
        val timeDialog: TimePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { v, hour, minute ->
                    time.set(year, month, dayOfMonth, hour, minute)
                    val textTime: String = (DateFormat.format("HH:mm", time)).toString()
                    view.text = textTime
                    when (view) {
                        mondayTime -> {
                            daysAndTime[buttonMonday] = time
                        }
                        tuesdayTime -> {
                            daysAndTime[buttonTuesday] = time
                        }
                        wednesdayTime -> {
                            daysAndTime[buttonWednesday] = time
                        }
                        thursdayTime -> {
                            daysAndTime[buttonThursday] = time
                        }
                        fridayTime -> {
                            daysAndTime[buttonFriday] = time
                        }
                        saturdayTime -> {
                            daysAndTime[buttonSaturday] = time
                        }
                        sundayTime -> {
                            daysAndTime[buttonSunday] = time
                        }
                    }
                },
                12,
                0,
                true)

        timeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        timeDialog.show()
    }

    private fun addEvent(day: CheckBox, time: Calendar, description: String) {
        val eventList: MutableList<Event> = mutableListOf()
        eventList.addAll(dailyEvent.dailyEvents)
        val event: Event = Event(description = description,
                year = year,
                month = month,
                dayOfMonth = dayOfMonth,
                dayOfWeek = 1,
                repeat = true,
                minute = time.get(Calendar.MINUTE),
                hour = time.get(Calendar.HOUR_OF_DAY))
        isCreateEventSuccessful = false
        when (day) {
            buttonMonday -> {
                if (mondayTime.text != "Time") {
                    dailyEvent.monday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.MONDAY
                }
            }
            buttonTuesday -> {
                if (tuesdayTime.text != "Time") {
                    dailyEvent.tuesday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.TUESDAY
                }
            }
            buttonWednesday -> {
                if (wednesdayTime.text != "Time") {
                    dailyEvent.wednesday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.WEDNESDAY
                }
            }
            buttonThursday -> {
                if (thursdayTime.text != "Time") {
                    dailyEvent.thursday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.THURSDAY
                }
            }
            buttonFriday -> {
                if (fridayTime.text != "Time") {
                    dailyEvent.friday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.FRIDAY
                }
            }
            buttonSaturday -> {
                if (saturdayTime.text != "Time") {
                    dailyEvent.saturday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.SATURDAY
                }
            }
            buttonSunday -> {
                if (sundayTime.text != "Time") {
                    dailyEvent.sunday = true
                    isCreateEventSuccessful = true
                    event.dayOfWeek = Calendar.SUNDAY
                }
            }
        }
        if (isCreateEventSuccessful) {
            eventList.add(event)
            dailyEvent.dailyEvents = eventList
        } else {
            Toast.makeText(this, "Add event time", Toast.LENGTH_SHORT).show()
        }
    }
}