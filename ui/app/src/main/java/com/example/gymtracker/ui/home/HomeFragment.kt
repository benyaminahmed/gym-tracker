package com.example.gymtracker.ui.home

import HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.R.id.search_mag_icon
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gymtracker.R
import com.example.gymtracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>
    private var exercisesList: List<String> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter with an empty list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())
        binding.exercisesListView.adapter = adapter

        homeViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            exercisesList = exercises.map { it.exerciseName }
            // Update the adapter's dataset directly without re-initializing it
            adapter.clear()
            adapter.addAll(exercisesList)
            adapter.notifyDataSetChanged()
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBarFetchExercises.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        setupSearchView()

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
                it.lowercase().contains(query.lowercase())
            }
            adapter.clear()
            adapter.addAll(filteredList)
            adapter.notifyDataSetChanged()
        } ?: run {
            // If query is null or empty, reset the list to the original dataset
            adapter.clear()
            adapter.addAll(exercisesList)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

