package com.example.healthapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.text.isEmpty

class HealthRecordAdapter(
    private var records: ArrayList<HealthRecord>,
    private val onItemClick: (HealthRecord) -> Unit,
    private val onItemDelete: (HealthRecord) -> Unit
) : RecyclerView.Adapter<HealthRecordAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weightText: TextView = view.findViewById(R.id.textWeight)
        val bpText: TextView = view.findViewById(R.id.textBloodPressure)
        val hrText: TextView = view.findViewById(R.id.textHeartRate)
        val notesText: TextView = view.findViewById(R.id.textNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.weightText.text = if (record.weight.isEmpty()) "N/A" else "${record.weight} kg"
        holder.bpText.text = if (record.bloodPressure.isEmpty()) "N/A" else record.bloodPressure
        holder.hrText.text = if (record.heartRate.isEmpty()) "N/A" else "${record.heartRate} bpm"
        holder.notesText.text = if (record.notes.isEmpty()) "No notes" else record.notes
        holder.itemView.setOnClickListener {
            onItemClick(record)
        }
        holder.itemView.setOnLongClickListener {
            onItemDelete(record)
            true
        }
    }

    override fun getItemCount(): Int {
        return records.size
    }

    fun updateList(newRecords: ArrayList<HealthRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }
}
