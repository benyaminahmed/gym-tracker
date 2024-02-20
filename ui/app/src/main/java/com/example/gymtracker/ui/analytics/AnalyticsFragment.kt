package com.example.gymtracker.ui.analytics

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gymtracker.databinding.FragmentAnalyticsBinding
import com.example.gymtracker.network.RetrofitService

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var exercisesViewModel: ExercisesViewModel
    private lateinit var adapter: ArrayAdapter<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Obtain the ExercisesViewModel from the factory
        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ExercisesViewModel::class.java)

        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter for the ListView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())
        binding.exercisesListView.adapter = adapter

        // Observe the distinct exercises with tracking from the ViewModel
        exercisesViewModel.trackedExercises.observe(viewLifecycleOwner) { distinctExercises ->
            // Update the adapter with the names of the distinct exercises
            val exerciseNames = distinctExercises.map { it.exerciseName }
            adapter.clear()
            adapter.addAll(exerciseNames)
            adapter.notifyDataSetChanged()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
