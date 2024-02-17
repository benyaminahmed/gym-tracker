package com.example.gymtracker.ui.home

import Exercise
import HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        // Setup Spinner
        homeViewModel.exercises.observe(viewLifecycleOwner, Observer<List<Exercise>> { exercises ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, exercises.map { it.exerciseName })
            binding.exercisesSpinner.adapter = adapter
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
