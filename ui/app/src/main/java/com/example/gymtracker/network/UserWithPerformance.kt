import com.example.gymtracker.network.ExerciseTracking
import com.example.gymtracker.network.User

data class UserWithPerformance(
    val user: User,
    val maxPerformance: ExerciseTracking? // Can be null if there's no performance data
)