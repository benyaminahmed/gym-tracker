package com.example.gymtracker.ui.exercises

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.R.id.search_mag_icon
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gymtracker.databinding.FragmentExercisesBinding
import com.example.gymtracker.network.Exercise
import com.example.gymtracker.network.RetrofitService

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>
    private var exercisesList: List<Exercise> = listOf()
    private var exercisesMap: Map<String, Exercise> = emptyMap()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        val exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter with an empty list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())
        binding.exercisesListView.adapter = adapter

        exercisesViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            exercisesList = exercises
            exercisesMap = exercises.associateBy { it.exerciseName }
            val exerciseNames = exercises.map { it.exerciseName }
            adapter.clear()
            adapter.addAll(exerciseNames)
            adapter.notifyDataSetChanged()
        }

        exercisesViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBarFetchExercises.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        setupSearchView()

        setupListViewItemClickListener()

        return root
    }
    private fun setupSearchView() {
        // Make the SearchView always expanded
        binding.searchExercise.apply {
            setIconifiedByDefault(false) // Always show the search text input

            // Optional: if you want to remove the search icon when always expanded
            findViewById<ImageView>(search_mag_icon)
                ?.apply {
                    layoutParams = LinearLayout.LayoutParams(0, 0)
                    visibility = View.GONE
                }

            binding.searchExercise.queryHint = "Search"

            // Set listeners for search text changes
            binding.searchExercise.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    performFiltering(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    performFiltering(newText)
                    return true
                }
            })
        }
    }

    private fun performFiltering(query: String?) {
        query?.let {
            val filteredList = exercisesList.filter {
                it.exerciseName.lowercase().contains(query.lowercase())
            }.map { it.exerciseName }
            exercisesMap = exercisesList.filter { it.exerciseName.lowercase().contains(query.lowercase()) }
                .associateBy { it.exerciseName }
            adapter.clear()
            adapter.addAll(filteredList)
            adapter.notifyDataSetChanged()
        } ?: run {
            exercisesMap = exercisesList.associateBy { it.exerciseName }
            adapter.clear()
            adapter.addAll(exercisesList.map { it.exerciseName })
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupListViewItemClickListener() {
        binding.exercisesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedExerciseName = adapter.getItem(position)
            selectedExerciseName?.let {
                val selectedExercise = exercisesMap[it]
                selectedExercise?.let { exercise ->
                    val action = ExercisesFragmentDirections.actionNavExercisesToExerciseDetailsFragment(
                        exercise.exerciseName, exercise.exerciseId.toString()
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

