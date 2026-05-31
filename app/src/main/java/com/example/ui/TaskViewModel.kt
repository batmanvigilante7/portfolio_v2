package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.TaskEntity
import com.example.data.TaskRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TaskRepository(db.taskDao())
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    private val _filter = MutableStateFlow("all") // "all", "todo", "done"
    val filter: StateFlow<String> = _filter.asStateFlow()

    private val _expandedPhases = MutableStateFlow<Set<Int>>(setOf(1)) // Phase 1 expanded by default
    val expandedPhases: StateFlow<Set<Int>> = _expandedPhases.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val filteredTasks: StateFlow<List<TaskEntity>> = combine(repository.allTasks, _filter) { tasks, currentFilter ->
        when (currentFilter) {
            "todo" -> tasks.filter { !it.isCompleted }
            "done" -> tasks.filter { it.isCompleted }
            else -> tasks
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCount: StateFlow<Int> = repository.allTasks.map { it.size }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val completedCount: StateFlow<Int> = repository.allTasks.map { it.count { t -> t.isCompleted } }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val progressPercent: StateFlow<Int> = repository.allTasks.map {
        val total = it.size
        if (total == 0) 0 else (it.count { t -> t.isCompleted } * 100) / total
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setFilter(newFilter: String) {
        _filter.value = newFilter
    }

    fun togglePhaseExpanded(phaseIdx: Int) {
        val current = _expandedPhases.value
        _expandedPhases.value = if (current.contains(phaseIdx)) {
            current - phaseIdx
        } else {
            current + phaseIdx
        }
    }

    fun setAllExpanded(expanded: Boolean) {
        _expandedPhases.value = if (expanded) {
            (1..7).toSet()
        } else {
            emptySet()
        }
    }

    fun toggleTask(id: String, completed: Boolean, taskText: String) {
        viewModelScope.launch {
            repository.toggleCompleted(id, completed)
            if (completed) {
                _toastMessage.value = "Marked complete: $taskText"
            }
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            _toastMessage.value = "Checklist progress reset."
        }
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    interface ExportCallback {
        fun onExportReady(jsonString: String)
    }

    fun getExportJsonString(callback: ExportCallback) {
        viewModelScope.launch {
            try {
                val tasks = repository.getAllTasksDirect()
                val progressMap = tasks.associate { it.id to it.isCompleted }
                val type = Types.newParameterizedType(Map::class.java, String::class.java, java.lang.Boolean::class.java)
                val jsonAdapter = moshi.adapter<Map<String, Boolean>>(type)
                val jsonString = jsonAdapter.toJson(progressMap)
                callback.onExportReady(jsonString)
            } catch (e: Exception) {
                _toastMessage.value = "Failed to export checklist."
            }
        }
    }

    fun importFromJsonString(jsonStr: String, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val type = Types.newParameterizedType(Map::class.java, String::class.java, java.lang.Boolean::class.java)
                val jsonAdapter = moshi.adapter<Map<String, Boolean>>(type)
                val stateMap = jsonAdapter.fromJson(jsonStr) ?: throw Exception("Null map parsed")
                val currentTasks = repository.getAllTasksDirect()
                val updatedTasks = currentTasks.map {
                    val completed = stateMap[it.id] ?: false
                    it.copy(isCompleted = completed)
                }
                repository.importTasks(updatedTasks)
                _toastMessage.value = "Checklist progress imported successfully."
            } catch (e: Exception) {
                onError()
            }
        }
    }
}
