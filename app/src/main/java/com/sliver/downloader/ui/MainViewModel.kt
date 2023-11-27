package com.sliver.downloader.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sliver.downloader.domain.DownloadRepository
import com.sliver.downloader.domain.DownloadRepositoryImpl
import com.sliver.downloader.domain.download.DownloadTask
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
            val task = repository.addNewDownloadTask(downloadUrl, object : DownloadTask.DownloadListener {
                override fun onProgressChanged(progress: Long, total: Long) {
                    val taskDetail = _taskDetails.value
                        .firstOrNull { it.taskUrl == downloadUrl } ?: return
                    val newTaskDetail = taskDetail.copy(
                        progress = progress,
                        progressTotal = total
                    )
                    _taskDetails.update { it - taskDetail + newTaskDetail }
                }

                override fun onStateChanged(state: TaskState) {
                    val taskDetail = _taskDetails.value
                        .firstOrNull { it.taskUrl == downloadUrl } ?: return
                    val newTaskDetail = taskDetail.copy(state = state)
                    _taskDetails.update { it - taskDetail + newTaskDetail }
                }
            })
            val taskDetail = TaskDetail(task.getSaveUrl(), task.getSaveFile().name)
            _taskDetails.update { it + taskDetail }
        }
    }

    fun deleteTask(detail: TaskDetail) {
        viewModelScope.launch {
            repository.cancelDownload(detail.taskUrl)
            _taskDetails.update { it.filter { it.taskUrl == detail.taskUrl }.toList() }
        }
    }

    data class TaskDetail(
        val taskUrl: String = "",
        val fileName: String = "",
        val progress: Long = 0,
        val progressTotal: Long = 0,
        val state: TaskState = TaskState.IDLE
    )

    enum class TaskState {
        IDLE, DOWNLOADING, PAUSED, COMPLETED
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repository = DownloadRepositoryImpl()
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
    }
}