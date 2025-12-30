package com.example.healthapp


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView;
    private lateinit var emptyText: TextView
    private lateinit var dbHelper: HealthDatabaseHelper
    private lateinit var adapter: HealthRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = HealthDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        emptyText = findViewById(R.id.textEmpty)

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        adapter = HealthRecordAdapter(ArrayList(),
            onItemClick = { record -> showEditDialog(record) },
            onItemDelete = { record -> deleteRecord(record) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        fabAdd.setOnClickListener { showAddDialog() }
        loadRecords()
    }

    private fun loadRecords() {
        val records = dbHelper.getAllRecords()
        adapter.updateList(records)
        emptyText.visibility = if (records.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showAddDialog() {
        showDialog(null)
    }

    private fun showEditDialog(record: HealthRecord) {
        showDialog(record)
    }

    private fun showDialog(record: HealthRecord?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_record, null)
        val editWeight = view.findViewById<EditText>(R.id.editWeight)
        val editBP = view.findViewById<EditText>(R.id.editBloodPressure)
        val editHR = view.findViewById<EditText>(R.id.editHeartRate)
        val editNotes = view.findViewById<EditText>(R.id.editNotes)
        val btnSave = view.findViewById<Button>(R.id.buttonSave)
        val btnCancel = view.findViewById<Button>(R.id.buttonCancel)
        val title = view.findViewById<TextView>(R.id.textDialogTitle)

        title.text = if (record == null) "Add Record" else "Edit Record"
        if (record != null) {
            editWeight.setText(record.weight)
            editBP.setText(record.bloodPressure)
            editHR.setText(record.heartRate)
            editNotes.setText(record.notes)
        }
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        btnSave.setOnClickListener {
            val weight = editWeight.text.toString().trim()
            val bp = editBP.text.toString().trim()
            val hr = editHR.text.toString().trim()
            val notes = editNotes.text.toString().trim()
            if (record == null) {
                dbHelper.addRecord(weight, bp, hr, notes)
                Toast.makeText(this, "Record added", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.updateRecord(record.id, weight, bp, hr, notes)
                Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
            loadRecords()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteRecord(record: HealthRecord) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Delete this record?")
            .setPositiveButton("Delete") { _, _ ->
                dbHelper.deleteRecord(record.id)
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                loadRecords()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
