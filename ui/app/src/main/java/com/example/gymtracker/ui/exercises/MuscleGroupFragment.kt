package com.example.gymtracker.ui.exercises

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gymtracker.databinding.FragmentMuscleGroupsBinding
import com.example.gymtracker.network.RetrofitService

class MuscleGroupsFragment : Fragment() {

    private var _binding: FragmentMuscleGroupsBinding? = null
    private val binding get() = _binding!!

    private lateinit var exercisesViewModel: ExercisesViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ExercisesViewModel::class.java)
        _binding = FragmentMuscleGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListView()
    }

    private fun setupListView() {
        // Create an ArrayAdapter using a simple list item layout and the muscleGroups LiveData
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())
        binding.muscleGroupsListView.adapter = adapter

        // When muscleGroups data changes, update the ArrayAdapter's data
        exercisesViewModel.muscleGroups.observe(viewLifecycleOwner) { muscleGroups ->
            adapter.clear()
            adapter.addAll(muscleGroups)
            adapter.notifyDataSetChanged()
        }

        // Handle list item clicks
        binding.muscleGroupsListView.setOnItemClickListener { _, _, position, _ ->
            val muscleGroup = adapter.getItem(position) ?: return@setOnItemClickListener
            val action = MuscleGroupsFragmentDirections.actionNavMuscleGroupsToNavExercises(muscleGroup)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
