import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.gymtracker.network.ApiService
import com.example.gymtracker.network.dto.Exercise
import com.example.gymtracker.network.dto.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.network.dto.ExerciseTracking
import com.example.gymtracker.network.dto.ExerciseTrackingRequest
import com.example.gymtracker.network.dto.ExerciseWithMuscleGroup
import com.example.gymtracker.network.dto.TrackedExercise
import kotlinx.coroutines.launch

class ExercisesViewModel(private val apiService: ApiService) : ViewModel() {

    private val _exercises = MutableLiveData<List<ExerciseWithMuscleGroup>>()
    val exercises: LiveData<List<ExerciseWithMuscleGroup>> = _exercises

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _exerciseTracking = MutableLiveData<List<ExerciseTracking>>()
    val exerciseTracking: LiveData<List<ExerciseTracking>> = _exerciseTracking

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _postResult = MutableLiveData<Boolean>()
    val postResult: LiveData<Boolean> = _postResult

    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> = _errorMessages

    // A LiveData property that will hold the distinct list of tracked exercises
    val trackedExercises: LiveData<List<TrackedExercise>> = _exerciseTracking.map { trackingList ->
        trackingList
            ?.map { TrackedExercise(it.exerciseId, it.exerciseName) }
            ?.distinct()
            ?.sortedBy { it.exerciseName }
            ?: emptyList()
    }

    // A LiveData property for distinct muscle groups
    // Sort alphabetically, "Other" last
    val muscleGroups: LiveData<List<String>> = _exercises.map { exercisesList ->
        val sortedList = exercisesList.orEmpty()
            .map { it.muscleGroup }
            .distinct()
            .sorted() // First, sort all alphabetically.
            .toMutableList()

        // If "Other" exists, remove and add it at the end.
        if (sortedList.remove("Other")) {
            sortedList.add("Other")
        }

        sortedList
    }


    init {
        refreshData()
    }

    fun refreshData() {
        fetchUsers()
        fetchExercises()
        fetchExerciseTracking()
    }

    private fun fetchExercises() {
        _isLoading.value = true
        apiService.getExercises().enqueue(object : Callback<List<ExerciseWithMuscleGroup>> {
            override fun onResponse(call: Call<List<ExerciseWithMuscleGroup>>, response: Response<List<ExerciseWithMuscleGroup>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val sortedExercises = response.body()?.sortedBy { it.exerciseName } ?: emptyList()
                    _exercises.postValue(sortedExercises)
                } else {
                    Log.e("ExercisesViewModel", "Failed to fetch exercises: ${response.errorBody()?.string()}")
                    _errorMessages.postValue("Failed to fetch exercises")
                }
            }

            override fun onFailure(call: Call<List<ExerciseWithMuscleGroup>>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessages.postValue("Error fetching exercises: ${t.message}")
            }
        })
    }

    private fun fetchUsers() {
        _isLoading.value = true
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val sortedUsers = response.body()?.sortedBy { it.firstName } ?: emptyList()
                    _users.postValue(sortedUsers)
                } else {
                    Log.e("ExercisesViewModel", "Failed to fetch users: ${response.errorBody()?.string()}")
                    _errorMessages.postValue("Failed to fetch users")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e("ExercisesViewModel", "Error fetching users", t)
                _errorMessages.postValue("Error fetching users: ${t.message}")
            }
        })
    }

    private fun fetchExerciseTracking() {
        apiService.getExerciseTracking().enqueue(object : Callback<List<ExerciseTracking>> {
            override fun onResponse(call: Call<List<ExerciseTracking>>, response: Response<List<ExerciseTracking>>) {
                if (response.isSuccessful) {
                    val sortedExercises = response.body()?.sortedBy { it.exerciseName } ?: emptyList()
                    _exerciseTracking.postValue(sortedExercises)
                } else {
                    Log.e("ExercisesViewModel", "Failed to fetch exercise tracking: ${response.errorBody()?.string()}")
                    _errorMessages.postValue("Failed to fetch exercise tracking")
                }
            }

            override fun onFailure(call: Call<List<ExerciseTracking>>, t: Throwable) {
                Log.e("ExercisesViewModel", "Error fetching users", t)
                _errorMessages.postValue("Failed to fetch exercise tracking")
            }
        })
    }

    fun postExerciseTracking(exerciseTrackingRequest: ExerciseTrackingRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.postExerciseTracking(exerciseTrackingRequest)
                if (response.isSuccessful) {
                    _postResult.postValue(true)
                } else {
                    _errorMessages.postValue("Failed to post exercise tracking")
                }
            } catch (e: Exception) {
                _errorMessages.postValue("Error posting exercise tracking: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

class ExercisesViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExercisesViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}