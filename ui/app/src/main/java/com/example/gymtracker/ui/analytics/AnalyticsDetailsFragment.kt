import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AnalyticsDetailsFragment : Fragment() {

    private lateinit var anyChartView: AnyChartView
    private lateinit var exerciseId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_analytics_details, container, false)

        // Initialize your AnyChartView from layout
        anyChartView = view.findViewById(R.id.any_chart_view)
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar))

        // Get exerciseId from arguments
        exerciseId = requireArguments().getString("exerciseId", "")

        // Now fetch the data for multiple users based on exerciseId and update the chart
        updateChartForExerciseId(exerciseId)

        return view
    }

    private fun updateChartForExerciseId(exerciseId: String) {
        val cartesian: Cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Exercise Performance Over Time")

        val usersData = fetchDataForExerciseId(exerciseId)

        for (userData in usersData) {
            val seriesData = ArrayList<DataEntry>()
            for (dataPoint in userData.dataPoints) {
                seriesData.add(ValueDataEntry(dataPoint.first, dataPoint.second))
            }
            val series: Line = cartesian.line(seriesData)
            series.name(userData.userId)
            series.hovered().markers().enabled(true)
            series.hovered().markers()
                .type("circle")
                .size(4.0)
                .stroke("1.5 #fff")
        }

        anyChartView.setChart(cartesian)
    }

    private fun fetchDataForExerciseId(exerciseId: String): List<UserExerciseData> {
        // Example data structure for user's exercise data
        // Replace this with actual data fetching logic
        return listOf(
            UserExerciseData("User1", listOf(Pair(1, 100), Pair(2, 200))),
            UserExerciseData("User2", listOf(Pair(1, 150), Pair(2, 250)))
        )
    }

}
