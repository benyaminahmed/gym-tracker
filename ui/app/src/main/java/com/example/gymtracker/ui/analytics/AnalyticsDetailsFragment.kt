package com.example.gymtracker.ui.analytics

import ExercisesViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.TooltipPositionMode
import com.example.gymtracker.R
import com.example.gymtracker.network.dto.ExerciseTracking
import java.text.SimpleDateFormat
import java.util.UUID

class AnalyticsDetailsFragment : Fragment() {


    private lateinit var exercisesViewModel: ExercisesViewModel

    private lateinit var anyChartView: AnyChartView
    private lateinit var exerciseId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_analytics_details, container, false)

        // Initialize your AnyChartView from layout
        anyChartView = view.findViewById(R.id.any_chart_view)

        // Get exerciseId from arguments
        exerciseId = requireArguments().getString("exerciseId", "")

        // Now fetch the data for multiple users based on exerciseId and update the chart
        // Observe the exerciseTracking LiveData or any other relevant data
        exercisesViewModel.exerciseTracking.observe(viewLifecycleOwner) { exerciseTrackingList ->
            // Assuming exerciseTrackingList is the data you need
            // Filter or process your list based on exerciseId if needed
            val filteredList = exerciseTrackingList.filter { it.exerciseId == UUID.fromString(exerciseId) }
            updateChart(filteredList)
        }

        return view
    }

    private fun updateChart(exerciseTrackingList: List<ExerciseTracking>) {
        val cartesian: Cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Exercise Performance Over Time")

        for (tracking in exerciseTrackingList) {
            val seriesData = ArrayList<DataEntry>()

            val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(tracking.createdDate) // Format the date
            seriesData.add(ValueDataEntry(formattedDate, tracking.performanceMetric))

            val series  = cartesian.line(seriesData)
            series.name(tracking.firstName)
            series.hovered().markers().enabled(true)
            series.hovered().markers()
                .type("circle")
                .size(4.0)
                .stroke("1.5 #fff")
        }

        anyChartView.setChart(cartesian)
    }
}
