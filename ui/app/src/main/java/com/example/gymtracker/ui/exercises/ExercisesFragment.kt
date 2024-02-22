package com.example.gymtracker.ui.exercises

import ExercisesViewModel
import ExercisesViewModelFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.R.id.search_mag_icon
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gymtracker.databinding.FragmentExercisesBinding
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.network.dto.ExerciseWithMuscleGroup
import com.example.gymtracker.utils.ErrorReporting

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>
    private var exercisesList: List<ExerciseWithMuscleGroup> = listOf()
    private var exercisesMap: Map<String, ExerciseWithMuscleGroup> = emptyMap()

    private lateinit var muscleGroup: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        val exercisesViewModel = ViewModelProvider(this, viewModelFactory).get(ExercisesViewModel::class.java)

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialise the adapter with an empty list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())
        binding.exercisesListView.adapter = adapter

        val args = ExercisesFragmentArgs.fromBundle(requireArguments())
        muscleGroup = args.muscleGroup

        exercisesViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            // Filter exercises by the received muscle group
            val filteredExercises = exercises.filter { it.muscleGroup == muscleGroup }
            exercisesMap = filteredExercises.associateBy { it.exerciseName }
            val exerciseNames = filteredExercises.map { it.exerciseName }
            adapter.clear()
            adapter.addAll(exerciseNames)
            adapter.notifyDataSetChanged()
        }

        exercisesViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBarFetchExercises.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        exercisesViewModel.errorMessages.observe(viewLifecycleOwner) { errorMessage ->
            ErrorReporting.showError(errorMessage, binding.root)
        }

        (activity as? AppCompatActivity)?.supportActionBar?.title = muscleGroup

        setupSearchView()
        setupListViewItemClickListener()

        return root
    }
    private fun setupSearchView() {
        // Make the SearchView always expanded
        binding.searchExercise.apply {
            setIconifiedByDefault(false)

            findViewById<ImageView>(search_mag_icon)
                ?.apply {
                    layoutParams = LinearLayout.LayoutParams(0, 0)
                    visibility = View.GONE
                }

            binding.searchExercise.queryHint = "Search"

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
            selectedExerciseName?.let { name ->
                val selectedExercise = exercisesMap[name]
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

