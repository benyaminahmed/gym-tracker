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
import com.example.gymtracker.network.dto.TrackedExercise
import kotlinx.coroutines.launch

class ExercisesViewModel(private val apiService: ApiService) : ViewModel() {

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _exerciseTracking = MutableLiveData<List<ExerciseTracking>>()
    val exerciseTracking: LiveData<List<ExerciseTracking>> = _exerciseTracking

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var exercisesList: List<Exercise> = listOf()

    private val _postResult = MutableLiveData<Boolean>()
    val postResult: LiveData<Boolean> = _postResult

    // A LiveData property that will hold the distinct list of tracked exercises
    private val _trackedExercises = MutableLiveData<List<TrackedExercise>>()

    val trackedExercises: LiveData<List<TrackedExercise>> = _exerciseTracking.map { trackingList ->
        trackingList?.map { TrackedExercise(it.exerciseId, it.exerciseName) }?.distinct() ?: emptyList()

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
        apiService.getExercises().enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    exercisesList = response.body()?.sortedBy { it.exerciseName } ?: listOf()
                    _exercises.postValue(exercisesList)
                } else {
                    Log.e("ExercisesViewModel", "Failed to fetch exercises: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e("ExercisesViewModel", "Error fetching exercises", t)
            }
        })
    }

    private fun fetchUsers() {
        _isLoading.value = true
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _users.postValue(response.body())
                } else {
                    Log.e("ExercisesViewModel", "Failed to fetch users: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e("ExercisesViewModel", "Error fetching users", t)
            }
        })
    }

    private fun fetchExerciseTracking() {
        apiService.getExerciseTracking().enqueue(object : Callback<List<ExerciseTracking>> {
            override fun onResponse(call: Call<List<ExerciseTracking>>, response: Response<List<ExerciseTracking>>) {
                if (response.isSuccessful) {
                    _exerciseTracking.postValue(response.body())
                } else {
                    Log.e("ExercisesViewModel", "Failed to fetch exercise tracking: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ExerciseTracking>>, t: Throwable) {
                Log.e("ExercisesViewModel", "Error fetching users", t)
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
                    _postResult.postValue(false)
                }
            } catch (e: Exception) {
                _postResult.postValue(false)
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