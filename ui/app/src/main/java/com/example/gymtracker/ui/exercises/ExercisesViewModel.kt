import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gymtracker.network.ApiService
import com.example.gymtracker.network.Exercise
import com.example.gymtracker.network.RetrofitService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ExercisesViewModel : ViewModel() {


    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        val apiService = RetrofitService.apiService

        apiService.getExercises().enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    _exercises.postValue(response.body())
                } else {
                    Log.e("Home", "Failed to fetch exercises")
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Log.e("Home", "Failed to fetch exercises", t)
            }

        })
    }
}
