package com.example.gymtracker.ui.analytics

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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
import com.example.gymtracker.databinding.FragmentAnalyticsDetailsBinding
import com.example.gymtracker.databinding.FragmentExercisesBinding
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.network.dto.ExerciseTracking
import com.example.gymtracker.ui.exercises.ExerciseDetailsFragmentArgs
import com.example.gymtracker.utils.ErrorReporting
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID

class AnalyticsDetailsFragment : Fragment() {


    private var _binding: FragmentAnalyticsDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var exercisesViewModel: ExercisesViewModel

    private lateinit var anyChartView: AnyChartView
    private lateinit var exerciseId: String
    private lateinit var progressBar: ProgressBar

    private val args: ExerciseDetailsFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_analytics_details, container, false)

        // Initialize ProgressBar from layout
        progressBar = view.findViewById(R.id.pbLoadingAnalyticsDetails)

        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

        // Initialize AnyChartView from layout
        anyChartView = view.findViewById(R.id.any_chart_view)

        // Initially hide the AnyChartView while data is being fetched
        anyChartView.visibility = View.INVISIBLE

        // Get exerciseId from arguments
        exerciseId = requireArguments().getString("exerciseId", "")

        // Show the ProgressBar when starting to fetch data
        progressBar.visibility = View.VISIBLE

        // Now fetch the data for multiple users based on exerciseId and update the chart
        // Observe the exerciseTracking LiveData or any other relevant data
        exercisesViewModel.exerciseTracking.observe(viewLifecycleOwner) { exerciseTrackingList ->
            val filteredList = exerciseTrackingList.filter { it.exerciseId == UUID.fromString(exerciseId) }
            updateChart(filteredList)
            progressBar.visibility = View.GONE
            anyChartView.visibility = View.VISIBLE
        }

        exercisesViewModel.errorMessages.observe(viewLifecycleOwner) { errorMessage ->
            ErrorReporting.showError(errorMessage, binding.root)
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
        cartesian.background().fill("#121212")  // Dark background
        cartesian.background().stroke("#2A2A2A")

        val unit = exerciseTrackingList.first().unit
        cartesian.title("Average Monthly Performance ($unit)")

        // Identify all unique time periods (YearMonth), sorted
        val allDates = exerciseTrackingList.map { YearMonth.from(it.createdDate) }.toSortedSet()

        // Group tracking data by user
        val trackingByUser = exerciseTrackingList.groupBy { it.firstName }

        trackingByUser.forEach { (firstName, trackingList) ->
            val monthlyAverages = calculateMonthlyAverages(trackingList)

            // Prepare series data, ensuring each time period is covered
            val sortedDates = allDates.toList()
            val seriesData: MutableList<DataEntry> = mutableListOf()
            var lastKnownAverage: Double? = null

            for (date in sortedDates) {
                val average = monthlyAverages[date]
                if (average != null) {
                    lastKnownAverage = average
                    // Format the date as MM/YY
                    val formattedDate = date.format(DateTimeFormatter.ofPattern("MM/yy"))
                    seriesData.add(ValueDataEntry(formattedDate, average))
                } else if (lastKnownAverage != null) {
                    // Use the last known average if the current date doesn't have data
                    val formattedDate = date.format(DateTimeFormatter.ofPattern("MM/yy"))
                    seriesData.add(ValueDataEntry(formattedDate, lastKnownAverage))
                }
            }

            val set = Set.instantiate()
            set.data(seriesData)
            val seriesMapping = set.mapAs("{ x: 'x', value: 'value' }")

            val series = cartesian.line(seriesMapping)
            series.name(firstName)
            series.tooltip()
                .format("$firstName: {%Value} $unit")
            series.hovered().markers().enabled(true)
            series.hovered().markers().type(MarkerType.CIRCLE).size(4.0).stroke("1.5 #000")
        }

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateMonthlyAverages(exerciseTrackingList: List<ExerciseTracking>): Map<YearMonth, Double> {
        val groupedByYearMonth = exerciseTrackingList.groupBy {
            YearMonth.from(it.createdDate)
        }

        return groupedByYearMonth.mapValues { (_, values) ->
            BigDecimal(values.map { it.performanceMetric }.average())
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
        }
    }
}
