import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Exercise(
    val exerciseId: String,
    val exerciseName: String,
    val unit: String,
    val createdDate: String
)

class HomeViewModel : ViewModel() {

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    init {
        fetchExercises()
    }

    private fun fetchExercises() {
        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:7195/") // Ensure this is your correct base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ExerciseService::class.java)

        service.getExercises().enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                if (response.isSuccessful) {
                    _exercises.postValue(response.body())
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                // Handle network failure
            }
        })
    }
}
