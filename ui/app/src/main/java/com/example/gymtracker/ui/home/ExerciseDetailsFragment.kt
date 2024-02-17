package com.example.gymtracker.ui.home

import UserAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.R.id.rvUsers
import com.example.gymtracker.R.id.tvExerciseTitle
import com.example.gymtracker.network.User
import java.util.UUID

class ExerciseDetailsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example on how to set the title
        val tvExerciseTitle = view.findViewById<TextView>(tvExerciseTitle)
        // Get the title from arguments or set a default one
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
        setupDateAndTimePickers()
    }

    private fun setupNumericInput() {
        // Your logic here
    }

    private fun setupSubmitButton() {
        // Your logic here
    }

    private fun setupDateAndTimePickers() {
        // Your logic here for date and time pickers
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
