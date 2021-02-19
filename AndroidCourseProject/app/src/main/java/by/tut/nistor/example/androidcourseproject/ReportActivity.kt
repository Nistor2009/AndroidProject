package by.tut.nistor.example.androidcourseproject

import android.R.attr.data
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate


class ReportActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout
    private lateinit var chart: PieChart
    private lateinit var yData: MutableList<Float>
    private lateinit var xData: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.viewReportToolbar)
        toolbar.setTitle(R.string.report)
        setSupportActionBar(toolbar)

        //Графика

        yData = mutableListOf(5f, 10f, 15f, 30f, 40f)
        layout = findViewById(R.id.headline)
        xData = mutableListOf("Sony", "Huawei", "LG", "Apple", "Samsung")
        chart = PieChart(this)
        layout.addView(chart)
        chart.setUsePercentValues(true)
        val desc: Description = Description()
        desc.text = "Smartphones Market Share"
        chart.description = desc
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.TRANSPARENT)
        chart.holeRadius = 7f
        chart.transparentCircleRadius = 10f

        // Разрешить поворот при касании
        chart.rotationAngle = 0f
        chart.isRotationEnabled = true

        // Listeners - можно в onValueSelected вписать действие по нажатию на сектор
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
            }

            override fun onNothingSelected() {
            }
        })

        // легенда - хз, что это
        val legend: Legend = chart.legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 5f

        val piaEntry1: PieEntry = PieEntry(12f, "First")
        val piaEntry2: PieEntry = PieEntry(30f, "Second")
        val list: MutableList<PieEntry> = mutableListOf(piaEntry1, piaEntry2)

        val dataSet: PieDataSet = PieDataSet(list, "label")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        val colorsList: MutableList<Int> = mutableListOf()
        ColorTemplate.VORDIPLOM_COLORS.forEach { colorsList.add(it) }
        ColorTemplate.JOYFUL_COLORS.forEach { colorsList.add(it) }
        ColorTemplate.COLORFUL_COLORS.forEach { colorsList.add(it) }
        ColorTemplate.LIBERTY_COLORS.forEach { colorsList.add(it) }
        ColorTemplate.PASTEL_COLORS.forEach { colorsList.add(it) }
        colorsList.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colorsList
        val pieData: PieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        chart.data = pieData;

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

}