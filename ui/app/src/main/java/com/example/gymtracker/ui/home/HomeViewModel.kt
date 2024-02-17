import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Exercise(
    val exerciseId: String,
    val exerciseName: String,
    val unit: String,
    val createdDate: String
)

class HomeViewModel : ViewModel() {

    private val _exercises = MutableLiveData<List<Exercise>>().apply {
        value = listOf(
            Exercise("3fa85f64-5717-4562-b3fc-2c963f66afa6", "Push-up", "Reps", "2024-02-17T09:49:58.635Z"),
            Exercise("4fa85f64-5717-4562-b3fc-2c963f66afa7", "Squat", "Reps", "2024-02-17T09:50:58.635Z"),
            // Add more exercises here
        )
    }
    val exercises: LiveData<List<Exercise>> = _exercises
}
