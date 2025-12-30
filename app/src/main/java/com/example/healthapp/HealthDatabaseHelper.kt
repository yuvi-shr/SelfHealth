package com.example.healthapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HealthDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "health.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE records (id INTEGER PRIMARY KEY, weight TEXT, bp TEXT, hr TEXT, notes TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS records")
        onCreate(db)
    }

    fun addRecord(weight: String, bp: String, hr: String, notes: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("weight", weight)
        values.put("bp", bp)
        values.put("hr", hr)
        values.put("notes", notes)
        db.insert("records", null, values)
    }

    fun getAllRecords(): ArrayList<HealthRecord> {
        val list = kotlin.collections.ArrayList<HealthRecord>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM records ORDER BY id DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val weight = cursor.getString(1)
                val bp = cursor.getString(2)
                val hr = cursor.getString(3)
                val notes = cursor.getString(4)
                list.add(HealthRecord(id, weight, bp, hr, notes))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateRecord(id: Long, weight: String, bp: String, hr: String, notes: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("weight", weight)
        values.put("bp", bp)
        values.put("hr", hr)
        values.put("notes", notes)
        db.update("records", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteRecord(id: Long) {
        val db = writableDatabase
        db.delete("records", "id=?", arrayOf(id.toString()))
    }
}
