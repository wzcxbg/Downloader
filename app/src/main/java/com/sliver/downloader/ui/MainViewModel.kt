package com.sliver.downloader.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sliver.downloader.domain.DownloadRepository
import com.sliver.downloader.domain.DownloadRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: DownloadRepository
) : ViewModel() {
    private val _taskDetails = MutableStateFlow<List<TaskDetail>>(listOf())
    val taskDetails = _taskDetails.asStateFlow()

    fun addNewTask(downloadUrl: String) {
        viewModelScope.launch {
            val task = repository.addNewDownloadTask(downloadUrl)
            _taskDetails.update { it + TaskDetail(downloadUrl, 0, TaskState.IDLE) }
        }
    }

    fun deleteTask(detail: TaskDetail) {
        viewModelScope.launch {
            repository.cancelDownload(detail.taskId)
            _taskDetails.update { it.filter { it.taskId == detail.taskId }.toList() }
        }
    }

    data class TaskDetail(
        val taskId: String = "UNKNOWN_ID",
        val progress: Int = 0,
        val state: TaskState = TaskState.IDLE
    )

    enum class TaskState {
        IDLE, DOWNLOADING, PAUSED, COMPLETED
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repository = DownloadRepositoryImpl()
            return MainViewModel(repository) as T
        }
    }
}