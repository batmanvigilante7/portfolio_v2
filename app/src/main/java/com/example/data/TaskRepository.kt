package com.example.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasksFlow()

    suspend fun checkAndSeedDatabase() {
        val current = taskDao.getAllTasksDirect()
        if (current.isEmpty()) {
            val seed = SeedData.getSeedTasks()
            taskDao.insertAll(seed)
        }
    }

    suspend fun toggleCompleted(id: String, completed: Boolean) {
        taskDao.toggleCompleted(id, completed)
    }

    suspend fun resetAllProgress() {
        taskDao.resetAllProgress()
    }

    suspend fun importTasks(tasks: List<TaskEntity>) {
        // Find existing tasks to preserve other fields if needed, or clear and insert full list
        taskDao.clearAll()
        taskDao.insertAll(tasks)
    }

    suspend fun getAllTasksDirect(): List<TaskEntity> {
        return taskDao.getAllTasksDirect()
    }
}
