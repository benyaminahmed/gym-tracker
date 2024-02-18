package com.example.gymtracker.ui.exercises

import ExercisesViewModel
import ExercisesViewModelFactory
import UserAdapter
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.R.id.rvUsers
import com.example.gymtracker.R.id.tvExerciseTitle
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.network.User
import java.util.Locale
import java.util.UUID

class ExerciseDetailsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        val exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

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
        tvExerciseTitle.text = arguments?.getString("exerciseTitle") ?: "Exercise"

        // Setup RecyclerView with an adapter to display users
        val rvUsers = view.findViewById<RecyclerView>(R.id.rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(context)


        // Reference to ProgressBar, EditText for achievement, and Submit Button
        val pbLoadingUsers = view.findViewById<ProgressBar>(R.id.pbLoadingUsersExerciseDetails)
        val etNumericInput = view.findViewById<EditText>(R.id.etNumericInput)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)


        // Observe the users LiveData from the ViewModel
        exercisesViewModel.users.observe(viewLifecycleOwner) { users ->
            val adapter = UserAdapter(users) { selectedUser ->
                // Handle user selection, e.g., update UI or perform an action with the selected user
            }
            rvUsers.adapter = adapter
            pbLoadingUsers.visibility = View.GONE // Hide loading symbol once users are loaded
        }

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

        // Handle numeric input, submit button, and date/time pickers
        setupNumericInput()
        setupSubmitButton()
        setupDatePickers()
    }

    private fun setupNumericInput() {
        // Your logic here
    }

    private fun setupSubmitButton() {
        // Your logic here
    }

    private fun setupDatePickers() {
        // Your logic here

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
