package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String, // format: "ph_${phaseIndex}_sec_${sectionIndex}_task_${taskIndex}"
    val phaseIndex: Int,
    val phaseName: String,
    val phaseNote: String,
    val sectionLabel: String,
    val taskText: String,
    val isCompleted: Boolean = false,
    val priority: String // "critical", "important", or "later"
)
