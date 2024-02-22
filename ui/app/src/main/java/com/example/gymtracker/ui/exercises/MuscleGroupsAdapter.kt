package com.example.gymtracker.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R

class MuscleGroupsAdapter(
    private val muscleGroups: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<MuscleGroupsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val muscleGroupTextView: TextView = view.findViewById(R.id.muscle_group_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_muscle_groups, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val muscleGroup = muscleGroups[position]
        holder.muscleGroupTextView.text = muscleGroup
        holder.itemView.setOnClickListener { onClick(muscleGroup) }
    }

    override fun getItemCount(): Int = muscleGroups.size
}
