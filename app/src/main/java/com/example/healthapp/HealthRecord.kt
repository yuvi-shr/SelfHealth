package com.example.healthapp

data class HealthRecord(
    val id: Long = 0,
    val weight: String = "",
    val bloodPressure: String = "",
    val heartRate: String = "",
    val notes: String = ""
)
