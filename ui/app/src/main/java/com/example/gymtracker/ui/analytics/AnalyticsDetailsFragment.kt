package com.example.gymtracker.ui.analytics

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.network.dto.ExerciseTracking
import com.example.gymtracker.ui.exercises.ExerciseDetailsFragmentArgs
import com.example.gymtracker.utils.ErrorReporting
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

        // Initialise ProgressBar from layout
        progressBar = view.findViewById(R.id.pbLoadingAnalyticsDetails)

        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

        // Initialise AnyChartView from layout
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
        cartesian.title("Performance Over Time ($unit)")

        // Predefined colour map
        val userColors = mutableMapOf<String, String>().apply {
            put("Imran", "#C71585") // Bright Purple
            put("Yusuf", "#00CED1") // Teal
            put("Benyamin", "#00FF00") // Lime Green
        }

        val allUserNames = exerciseTrackingList.map { it.firstName }.distinct()
        val colorsPool = listOf("#ff0000", "#00ff00", "#0000ff", "#ffff00", "#ff00ff", "#00ffff", "#000000") // Example colors
        allUserNames.forEachIndexed { index, userName ->
            userColors.putIfAbsent(userName, colorsPool[index % colorsPool.size])
        }

        // Identify all unique dates (sorted)
        val allDates = exerciseTrackingList.map { it.createdDate.toLocalDate() }.sorted()

        // Group tracking data by user
        val trackingByUser = exerciseTrackingList.groupBy { it.firstName }

        trackingByUser.forEach { (firstName, trackingList) ->
            val sortedTrackingList = trackingList.sortedBy { it.createdDate }

            // Prepare series data, ensuring each date is covered
            val seriesData: MutableList<DataEntry> = mutableListOf()
            var lastKnownMetric: Double? = null

            for (date in allDates) {
                val trackingForDate = sortedTrackingList.find { it.createdDate.toLocalDate() == date }
                if (trackingForDate != null) {
                    lastKnownMetric = trackingForDate.performanceMetric
                }

                // Format the date as dd/MM/YY for readability
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
                if (lastKnownMetric != null) {
                    seriesData.add(ValueDataEntry(formattedDate, lastKnownMetric))
                }
            }

            val set = Set.instantiate()
            set.data(seriesData)
            val seriesMapping = set.mapAs("{ x: 'x', value: 'value' }")

            val series = cartesian.line(seriesMapping)
            series.name(firstName)
            series.tooltip().format("$firstName: {%Value} $unit")
            series.hovered().markers().enabled(true)
            series.hovered().markers().type(MarkerType.CIRCLE).size(4.0).stroke("1.5 #000")

            // Set series colour from the map
            userColors[firstName]?.let { seriesColor ->
                series.color(seriesColor)
            }
        }

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)
    }
}
