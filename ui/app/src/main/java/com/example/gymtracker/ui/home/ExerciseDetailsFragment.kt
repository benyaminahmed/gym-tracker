package com.example.gymtracker.ui.home

import UserAdapter
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.R.id.rvUsers
import com.example.gymtracker.R.id.tvExerciseTitle
import com.example.gymtracker.network.User
import com.google.android.material.button.MaterialButton
import java.util.UUID

class ExerciseDetailsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDateTitle: TextView = view.findViewById(R.id.tvDateTitle)
        val currentDate = Calendar.getInstance()
        val currentDateString = "${currentDate.get(Calendar.DAY_OF_MONTH)}/${currentDate.get(Calendar.MONTH) + 1}/${currentDate.get(Calendar.YEAR)}"
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
        val mockUsers = listOf(
            User(UUID.randomUUID(), "John", "Doe"),
            User(UUID.randomUUID(), "Jane", "Doe"),
            User(UUID.randomUUID(), "Jim", "Beam")
        )

        val rvUsers = view.findViewById<RecyclerView>(rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(context)
        rvUsers.adapter = UserAdapter(mockUsers)

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
