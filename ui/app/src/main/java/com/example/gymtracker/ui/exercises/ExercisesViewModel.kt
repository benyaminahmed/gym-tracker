import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.gymtracker.network.ApiService
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.network.Exercise
import com.example.gymtracker.network.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ExercisesViewModel : ViewModel() {

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchUsers()
        fetchExercises()
    }

    private fun fetchExercises() {
        _isLoading.value = true
        val apiService = RetrofitService.apiService
        apiService.getExercises().enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _exercises.postValue(response.body())
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
        val apiService = RetrofitService.apiService
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
}
