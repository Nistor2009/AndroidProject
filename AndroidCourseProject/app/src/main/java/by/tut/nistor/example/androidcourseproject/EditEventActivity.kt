package by.tut.nistor.example.androidcourseproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import by.tut.nistor.example.androidcourseproject.eventdata.Event
import java.util.*

const val editEvent: String = "event"

class EditEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        val event: Event = intent.getSerializableExtra(editEvent) as Event
        // получаем и устанавливаем данные во вью
        val viewItemEventDescription: TextView = findViewById(R.id.viewTextDescriptionInEditActivity)
        viewItemEventDescription.text = event.description
        viewItemEventDescription.movementMethod = ScrollingMovementMethod()
        val viewTextEventDateInEditActivity: TextView = findViewById(R.id.viewTextEventDateInEditActivity)
        viewTextEventDateInEditActivity.text = "${event.dayOfMonth}.${event.month}.${event.year}"
        val viewTextEventTimeInEditActivity: TextView = findViewById(R.id.viewTextEventTimeInEditActivity)
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(0, 0, 0, event.hour, event.minute)
        val textTime: String = (DateFormat.format("HH:mm", calendar)).toString()
        viewTextEventTimeInEditActivity.text = textTime

        val deleteButton: Button = findViewById(R.id.viewButtonDelete)
        deleteButton.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent()
            intent.putExtra("Deleted event", event)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })

    }


}