package by.tut.nistor.example.androidcourseproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.tut.nistor.example.androidcourseproject.alarm.BroadcastAlarm
import by.tut.nistor.example.androidcourseproject.database.DbBuilder
import by.tut.nistor.example.androidcourseproject.eventdata.DailyEvent
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import by.tut.nistor.example.androidcourseproject.viewmodel.EventModelFactory
import by.tut.nistor.example.androidcourseproject.viewmodel.EventViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private var eventListForToday: List<Event> = Collections.emptyList()
    private lateinit var viewModel: EventViewModel
    private lateinit var radioGroup: RadioGroup
    private val CHECKED_ALL_LIST: Int = 1
    private val CHECKED_LIST_FOR_TODAY: Int = 2
    private val CHECKED_DAILY_LIST: Int = 3
    private var checkedList: Int = CHECKED_LIST_FOR_TODAY
    private val CREATE_ACTIVITY_CODE: Int = 111
    private val EDIT_ACTIVITY_CODE: Int = 222
    private val CREATE_DAILY_ACTIVITY_CODE: Int = 333
    private val CREATE_REPORT_ACTIVITY_CODE: Int = 444
    private lateinit var broadcastAlarm: BroadcastAlarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioGroup = findViewById(R.id.viewRadioCheckUpdateList)
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkerId ->
            when (checkerId) {
                R.id.viewRadioListForToday -> {
                    checkedList = CHECKED_LIST_FOR_TODAY
                    updateEventList(checkedList)
                }
                R.id.viewRadioAllList -> {
                    checkedList = CHECKED_ALL_LIST
                    updateEventList(checkedList)
                }
                R.id.viewRadioDailyEvents -> {
                    checkedList = CHECKED_DAILY_LIST
                    updateEventList(checkedList)
                }
            }
        })

        //создать базу данных
        val builder: DbBuilder = DbBuilder
        builder.initDatabase(this)

        var adapter: RecyclerViewAdapter = RecyclerViewAdapter(eventListForToday, object : OnItemClickListener {
            override fun onItemClick(event: Event) {
                startEditEventActivity(event)
            }
        })
        viewModel = ViewModelProvider(this, EventModelFactory()).get(EventViewModel::class.java)
        viewModel.liveData.observe(this, Observer { data ->
            eventListForToday = data
            adapter.updateEventList(eventListForToday)
            startAlarm(eventListForToday)
        })
        updateEventList(checkedList)

        //подключаем RecyclerView со списком задач на главном экране
        val viewRecyclerEventList: RecyclerView = findViewById(R.id.viewRecyclerEventList)
        viewRecyclerEventList.adapter = adapter
        viewRecyclerEventList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        //подключаем Toolbar
        val toolbar: Toolbar = findViewById(R.id.viewReportToolbar)
        setSupportActionBar(toolbar)
    }

    //подключаем меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.menuCreateEvent) {
            val intent: Intent = Intent(this, CreateEventActivity::class.java)
            startActivityForResult(intent, CREATE_ACTIVITY_CODE)
        }
        if (itemId == R.id.menuDaily_event) {
            val intent: Intent = Intent(this, CreateDailyEventActivity::class.java)
            startActivityForResult(intent, CREATE_DAILY_ACTIVITY_CODE)
        }
        if (itemId == R.id.menuReport) {
            val intent: Intent = Intent(this, ReportActivity::class.java)
            startActivityForResult(intent, CREATE_REPORT_ACTIVITY_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (requestCode == CREATE_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            val event: Event = data.getSerializableExtra("Created event") as Event
            viewModel.addEvent(event)
            Thread.sleep(50)
            updateEventList(checkedList)
        }
        if (requestCode == EDIT_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            val event: Event = data.getSerializableExtra("Deleted event") as Event
            viewModel.deleteEvent(event)
            Thread.sleep(50)
            updateEventList(checkedList)
        }
        if (requestCode == CREATE_DAILY_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            val dailyEvent: DailyEvent = data.getSerializableExtra("Created daily event") as DailyEvent
            viewModel.addDailyEvent(dailyEvent)
            Log.d("LOG", dailyEvent.toString())
            Thread.sleep(50)
            updateEventList(checkedList)
        }
        if (requestCode == CREATE_REPORT_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
//            val dailyEvent: DailyEvent = data.getSerializableExtra("Report") as DailyEvent
//            viewModel.addDailyEvent(dailyEvent)
//            Log.d("LOG", dailyEvent.toString())
//            Thread.sleep(50)
//            updateEventList(checkedList)
        }
    }

    private fun startAlarm(eventListForToday: List<Event>) {

        // подключаем receiver
        Log.d("LOG", " startAlarm")
        broadcastAlarm = BroadcastAlarm()
        broadcastAlarm.setOnetimeTimer(this.applicationContext, eventListForToday)
    }

    private fun updateEventList(checkedList: Int) {
        when (checkedList) {
            CHECKED_ALL_LIST -> {
                viewModel.getAllEvents()
            }
            CHECKED_LIST_FOR_TODAY -> {
                viewModel.getEventFromToday()
            }
            CHECKED_DAILY_LIST -> {
                viewModel.getAllDailyEvents()
            }
        }
    }

    private fun startEditEventActivity(event: Event) {
        val intent = Intent(this, EditEventActivity::class.java)
        intent.putExtra("event", event)
        startActivityForResult(intent, EDIT_ACTIVITY_CODE)
    }

    interface OnItemClickListener {
        fun onItemClick(event: Event)
    }

    // класс-адаптер для RecyclerView
    private class RecyclerViewAdapter(var eventList: List<Event>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

        fun updateEventList(eventList: List<Event>) {
            this.eventList = eventList
            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
            return RecyclerViewHolder(view)
        }

        override fun getItemCount(): Int = eventList.size


        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.bind(eventList[position], listener)
        }

        private class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val viewItemEventDescription: TextView = itemView.findViewById(R.id.viewItemEventDescription)
            private val viewItemEventData: TextView = itemView.findViewById(R.id.viewItemEventData)
            private val viewItemEventTime: TextView = itemView.findViewById(R.id.viewItemEventTime)
            private val itemLayout: LinearLayout = itemView.findViewById(R.id.itemLayout)

            fun bind(event: Event, listener: OnItemClickListener) {
                val year: Int = event.year
                val month: Int = event.month
                val dayOfMonth: Int = event.dayOfMonth
                var date: String = "$dayOfMonth.$month.$year"
                if (event.repeat) {
                    when (event.dayOfWeek) {
                        Calendar.MONDAY -> {
                            date = "Monday"
                        }
                        Calendar.TUESDAY -> {
                            date = "Tuesday"
                        }
                        Calendar.WEDNESDAY -> {
                            date = "Wednesday"
                        }
                        Calendar.THURSDAY -> {
                            date = "Thursday"
                        }
                        Calendar.FRIDAY -> {
                            date = "Friday"
                        }
                        Calendar.SATURDAY -> {
                            date = "Saturday"
                        }
                        Calendar.SUNDAY -> {
                            date = "Sunday"
                        }
                    }
                }
                val hour: Int = event.hour
                val minute: Int = event.minute
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(0, 0, 0, hour, minute)
                val textTime: String = (DateFormat.format("HH:mm", calendar)).toString()
                viewItemEventDescription.text = event.description
                viewItemEventData.text = date
                viewItemEventTime.text = textTime
                itemLayout.setOnClickListener(View.OnClickListener {
                    listener.onItemClick(event)
                })
            }
        }
    }
}