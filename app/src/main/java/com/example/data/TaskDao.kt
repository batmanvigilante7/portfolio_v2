package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY phaseIndex ASC, id ASC")
    fun getAllTasksFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY phaseIndex ASC, id ASC")
    suspend fun getAllTasksDirect(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun toggleCompleted(id: String, isCompleted: Boolean)

    @Query("UPDATE tasks SET isCompleted = 0")
    suspend fun resetAllProgress()

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}
