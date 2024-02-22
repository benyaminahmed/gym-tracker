package com.example.gymtracker.ui.exercises

import ExercisesViewModel
import ExercisesViewModelFactory
import UserAdapter
import com.example.gymtracker.network.dto.UserWithPerformance
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.gymtracker.R
import com.example.gymtracker.databinding.FragmentExerciseDetailsBinding
import com.example.gymtracker.databinding.FragmentExercisesBinding
import com.example.gymtracker.network.dto.ExerciseTrackingRequest
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.utils.ErrorReporting
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class ExerciseDetailsFragment : Fragment() {


    private var _binding: FragmentExerciseDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: UserAdapter
    private var exerciseId: UUID? = null
    private lateinit var exercisesViewModel: ExercisesViewModel
    private lateinit var exerciseName: String

    private lateinit var pbLoadingUsers: ProgressBar
    private lateinit var etNumericInput: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnAnalytics: TextView
    private var isFirstLoad = true

    private val args: ExerciseDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentExerciseDetailsBinding.inflate(inflater, container, false)

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
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    val selectedDateString = dateFormat.format(selectedCalendar.time)
                    tvDateTitle.text = selectedDateString
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Get the title / id from arguments
        exerciseName = args.exerciseName
        (activity as? AppCompatActivity)?.supportActionBar?.title = exerciseName

        exerciseId = UUID.fromString(args.exerciseId )

        setupUserAdapterAndRecyclerView(view)

        // Reference to ProgressBar, EditText for achievement, and Submit Button
        pbLoadingUsers = view.findViewById<ProgressBar>(R.id.pbLoadingUsersExerciseDetails)
        etNumericInput = view.findViewById<EditText>(R.id.etNumericInput)
        btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        exercisesViewModel.errorMessages.observe(viewLifecycleOwner) { errorMessage ->
            ErrorReporting.showError(errorMessage, binding.root)
        }

        // Set-up analytics button
        btnAnalytics = view.findViewById<TextView>(R.id.btnAnalytics)

        btnAnalytics.setOnClickListener {
            val action = ExerciseDetailsFragmentDirections
                .actionExerciseDetailsFragmentToAnalyticsDetailsFragment(exerciseName, exerciseId.toString())
            findNavController().navigate(action)
        }
        refreshUserExerciseData()
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
                            refreshUserExerciseData()
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
            val number = input.toDouble()
            number > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun refreshUserExerciseData() {
        if (isFirstLoad) {
            pbLoadingUsers.visibility = View.VISIBLE
            etNumericInput.visibility = View.GONE
            btnSubmit.visibility = View.GONE
            btnAnalytics.visibility = View.GONE
            btnAnalytics.visibility = View.GONE
        }
        exercisesViewModel.refreshData()

        exercisesViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            exercisesViewModel.exerciseTracking.observe(viewLifecycleOwner) { exerciseTracking ->
                exercisesViewModel.users.observe(viewLifecycleOwner) { users ->
                    if (isFirstLoad) {
                        pbLoadingUsers.visibility = View.GONE
                        etNumericInput.visibility = View.VISIBLE
                        btnSubmit.visibility = View.VISIBLE
                        btnAnalytics.visibility = View.VISIBLE
                        isFirstLoad = false // Ensure this logic runs only once
                    }
                    exerciseId = exercises.firstOrNull { it.exerciseName == exerciseName }?.exerciseId
                    val combinedData = users.map { user ->
                        val maxPerformance = exerciseTracking
                            .filter { it.userId == user.userId && it.exerciseId == exerciseId }
                            .maxByOrNull { it.performanceMetric ?: 0.0 }
                        UserWithPerformance(user, maxPerformance)
                    }
                    if(exerciseTracking.none { it.exerciseId == exerciseId }) {
                        btnAnalytics.visibility = View.GONE
                    }
                    userAdapter.updateUsers(combinedData)
                }
            }
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
