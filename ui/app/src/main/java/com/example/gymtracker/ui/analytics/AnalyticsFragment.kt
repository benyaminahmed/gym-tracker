package com.example.gymtracker.ui.analytics

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
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gymtracker.databinding.FragmentAnalyticsBinding
import com.example.gymtracker.network.RetrofitService
import com.example.gymtracker.utils.ErrorReporting
import com.google.android.material.snackbar.Snackbar

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var exercisesViewModel: ExercisesViewModel
    private lateinit var adapter: ArrayAdapter<String>
    private var exercisesMap: Map<String, String> = emptyMap()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val apiService = RetrofitService.create(requireContext())
        val viewModelFactory = ExercisesViewModelFactory(apiService)
        exercisesViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ExercisesViewModel::class.java)

        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())
        binding.exercisesListView.adapter = adapter

        exercisesViewModel.trackedExercises.observe(viewLifecycleOwner) { distinctExercises ->
            val exerciseNames = distinctExercises.map { it.exerciseName }
            exercisesMap = distinctExercises.associate { it.exerciseName to it.exerciseId.toString() }
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

        setupSearchView()
        setupListViewItemClickListener()

        return root
    }


    private fun setupSearchView() {
        binding.searchExercise.apply {
            setIconifiedByDefault(false)

            findViewById<ImageView>(R.id.search_mag_icon)
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
            val filteredList = exercisesMap.keys.filter {
                it.lowercase().contains(query.lowercase())
            }
            adapter.clear()
            adapter.addAll(filteredList)
            adapter.notifyDataSetChanged()
        } ?: run {
            adapter.clear()
            adapter.addAll(exercisesMap.keys)
            adapter.notifyDataSetChanged()
        }
    }
    private fun setupListViewItemClickListener() {
        binding.exercisesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedExerciseName = adapter.getItem(position)
            selectedExerciseName?.let { name ->
                val selectedExerciseId = exercisesMap[name]
                selectedExerciseId?.let { id ->
                    val action = AnalyticsFragmentDirections.actionNavExercisesToAnalyticsDetailsFragment(name, id)
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
