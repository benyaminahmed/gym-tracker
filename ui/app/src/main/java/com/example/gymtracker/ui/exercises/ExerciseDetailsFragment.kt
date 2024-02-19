package com.example.gymtracker.ui.exercises

import ExercisesViewModel
import ExercisesViewModelFactory
import UserAdapter
import UserWithPerformance
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.R.id.tvExerciseTitle
import com.example.gymtracker.network.ExerciseTrackingRequest
import com.example.gymtracker.network.RetrofitService
import com.google.android.material.textview.MaterialTextView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class ExerciseDetailsFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private var exerciseId: UUID? = null
    private lateinit var exercisesViewModel: ExercisesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

        // Set date to default to current
        val tvDateTitle: TextView = view.findViewById(R.id.tvDateTitle)
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val currentDateString = dateFormat.format(currentDate.time)
        tvDateTitle.text = currentDateString

        val btnDatePicker: ImageButton = view.findViewById(R.id.btnDatePicker)

        btnDatePicker.setOnClickListener {
            val now = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    tvDateTitle.text = selectedDate
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Get the title from arguments or set a default one
        val tvExerciseTitle = view.findViewById<TextView>(tvExerciseTitle)
        val exerciseName =  arguments?.getString("exerciseTitle") ?: "Exercise";
        tvExerciseTitle.text = exerciseName

        setupUserAdapterAndRecyclerView(view)

        // Reference to ProgressBar, EditText for achievement, and Submit Button
        val pbLoadingUsers = view.findViewById<ProgressBar>(R.id.pbLoadingUsersExerciseDetails)
        val etNumericInput = view.findViewById<EditText>(R.id.etNumericInput)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        // Observe isLoading LiveData
        exercisesViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show loading spinner and hide EditText and Button
                pbLoadingUsers.visibility = View.VISIBLE
                etNumericInput.visibility = View.GONE
                btnSubmit.visibility = View.GONE
            } else {
                // Hide loading spinner and show EditText and Button
                pbLoadingUsers.visibility = View.GONE
                etNumericInput.visibility = View.VISIBLE
                btnSubmit.visibility = View.VISIBLE
            }
        }
        exercisesViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            exercisesViewModel.exerciseTracking.observe(viewLifecycleOwner) { exerciseTracking ->
                exercisesViewModel.users.observe(viewLifecycleOwner) { users ->
                    exerciseId = exercises.firstOrNull { it.exerciseName == exerciseName }?.exerciseId
                    val combinedData = users.map { user ->
                        val maxPerformance = exerciseTracking
                            .filter { it.userId == user.userId && it.exerciseId == exerciseId }
                            .maxByOrNull { it.performanceMetric ?: 0.0 }
                        UserWithPerformance(user, maxPerformance)
                    }
                    userAdapter.updateUsers(combinedData)
                    pbLoadingUsers.visibility = View.GONE
                }
            }
        }
        setUpSubmit(view)
    }

    private fun setupUserAdapterAndRecyclerView(view: View) {
        // Assuming rvUsers is your RecyclerView
        val rvUsers = view.findViewById<RecyclerView>(R.id.rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(context)

        // Initialize userAdapter here
        userAdapter = UserAdapter(emptyList()) { selectedUser ->
            // Handle user selection
        }
        rvUsers.adapter = userAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpSubmit(view: View) {
        val etNumericInput = view.findViewById<EditText>(R.id.etNumericInput)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        val tvDateTitle = view.findViewById<TextView>(R.id.tvDateTitle)

        btnSubmit.setOnClickListener {
            val selectedUser = userAdapter.getSelectedUser()
            if (selectedUser != null && validateInput(etNumericInput.text.toString())) {
                val performanceMetric = etNumericInput.text.toString().toDouble()
                val userId = selectedUser.user.userId

                val createdDate = createDateTimeString(tvDateTitle.text.toString())

                exerciseId?.let {
                    val exerciseTrackingRequest = ExerciseTrackingRequest(userId, it, performanceMetric, createdDate)

                    exercisesViewModel.postExerciseTracking(exerciseTrackingRequest)

                    // Observe the LiveData for post result
                    exercisesViewModel.postResult.observe(viewLifecycleOwner) { isSuccess ->
                        if (isSuccess) {
                            Toast.makeText(requireContext(), "Data submitted successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Submission failed", Toast.LENGTH_LONG).show()
                        }
                    }
                } ?: run {
                    Toast.makeText(requireContext(), "Exercise ID is missing.", Toast.LENGTH_LONG).show()
                }
            } else {
                if (selectedUser == null) Toast.makeText(requireContext(), "Please select a user", Toast.LENGTH_SHORT).show()
                if (!validateInput(etNumericInput.text.toString())) Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_LONG).show()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDateTimeString(dateString: String): String {
        // Parse the date from the dateString
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
        val parsedDate = LocalDate.parse(dateString, formatter)

        // Get today's date for comparison
        val today = LocalDate.now()

        // Determine the time component based on whether the date is today or a previous date
        val dateTime = if (parsedDate.isEqual(today)) {
            // If it's today, use the current time
            LocalDateTime.now()
        } else {
            // If it's a previous date, default to midnight
            LocalDateTime.of(parsedDate, LocalTime.MIDNIGHT)
        }

        // Format the LocalDateTime object to the required string format
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return dateTime.format(outputFormatter)
    }

    private fun validateInput(input: String): Boolean {
        return try {
            val number = input.toInt()
            number > 0
        } catch (e: NumberFormatException) {
            false // Input was not a number or was empty
        }
    }

    companion object {
        fun newInstance(exerciseTitle: String): ExerciseDetailsFragment {
            val fragment = ExerciseDetailsFragment()
            val args = Bundle()
            args.putString("exerciseTitle", exerciseTitle)
            fragment.arguments = args
            return fragment
        }
    }
}
