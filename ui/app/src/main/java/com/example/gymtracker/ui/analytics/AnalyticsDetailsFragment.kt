package com.example.gymtracker.ui.analytics

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.data.Set
import com.example.gymtracker.R
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.network.dto.ExerciseTracking
import com.example.gymtracker.ui.exercises.ExerciseDetailsFragmentArgs
import java.time.format.DateTimeFormatter
import java.util.UUID

class AnalyticsDetailsFragment : Fragment() {


    private lateinit var exercisesViewModel: ExercisesViewModel

    private lateinit var anyChartView: AnyChartView
    private lateinit var exerciseId: String

    private val args: ExerciseDetailsFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_analytics_details, container, false)

        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

        // Initialize AnyChartView from layout
        anyChartView = view.findViewById(R.id.any_chart_view)

        // Get exerciseId from arguments
        exerciseId = requireArguments().getString("exerciseId", "")

        // Now fetch the data for multiple users based on exerciseId and update the chart
        // Observe the exerciseTracking LiveData or any other relevant data
        exercisesViewModel.exerciseTracking.observe(viewLifecycleOwner) { exerciseTrackingList ->
            val filteredList = exerciseTrackingList.filter { it.exerciseId == UUID.fromString(exerciseId) }
            updateChart(filteredList)
        }

        (activity as? AppCompatActivity)?.supportActionBar?.title = args.exerciseName
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateChart(exerciseTrackingList: List<ExerciseTracking>) {
        val cartesian: Cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Performance Over Time")

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Group tracking data by user
        val trackingByUser = exerciseTrackingList.groupBy { it.firstName }

        // For each group, create a series
        for ((firstName, trackingList) in trackingByUser) {
            val seriesData: MutableList<DataEntry> = ArrayList()

            for (tracking in trackingList) {
                val formattedDate = tracking.createdDate.format(formatter)
                seriesData.add(ValueDataEntry(formattedDate, tracking.performanceMetric))
            }

            val set = Set.instantiate()
            set.data(seriesData)
            val seriesMapping = set.mapAs("{ x: 'x', value: 'value' }")

            val series = cartesian.line(seriesMapping)
            series.name(firstName)
            series.hovered().markers().enabled(true)
            series.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)
                .stroke("1.5 #000")
        }

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)
    }


}
