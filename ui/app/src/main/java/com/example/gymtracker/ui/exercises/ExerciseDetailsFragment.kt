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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.R.id.tvExerciseTitle
import com.example.gymtracker.network.RetrofitService
import java.util.Locale

class ExerciseDetailsFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter

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

        setupUserAdapterAndRecyclerView(view)

        // Reference to ProgressBar, EditText for achievement, and Submit Button
        val pbLoadingUsers = view.findViewById<ProgressBar>(R.id.pbLoadingUsersExerciseDetails)
        val etNumericInput = view.findViewById<EditText>(R.id.etNumericInput)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        // Observe the users LiveData from the ViewModel
        exercisesViewModel.users.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
            pbLoadingUsers.visibility = View.GONE
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

        setUpValidation(view)
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

        // Now userAdapter is initialized and can be safely used elsewhere
    }

    private fun setUpValidation(view: View) {
        val etNumericInput = view.findViewById<EditText>(R.id.etNumericInput)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val selectedUser = userAdapter.getSelectedUser()
            val numericInputValid = validateInput(etNumericInput.text.toString())

            if (selectedUser != null && numericInputValid) {
                // User is selected and numeric input is valid
                // Proceed with your logic, e.g., process data, navigate, etc.
            } else {
                if (selectedUser == null) {
                    Toast.makeText(requireContext(), "Please select a user", Toast.LENGTH_SHORT).show()
                }
                if(!numericInputValid) {
                    Toast.makeText(requireContext(), "Please enter a number greater than 0", Toast.LENGTH_LONG).show()

                }
             }
        }
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
