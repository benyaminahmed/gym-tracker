package com.example.gymtracker.ui.home

import HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gymtracker.R
import com.example.gymtracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup List View
        homeViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, exercises.map { it.exerciseName })
            binding.exercisesListView.adapter = adapter
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            val progressBar = root.findViewById<ProgressBar>(R.id.progress_bar_fetch_exercises)
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
