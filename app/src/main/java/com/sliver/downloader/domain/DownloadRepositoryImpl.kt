package com.sliver.downloader.domain

import com.sliver.downloader.domain.download.DownloadTask
import java.util.concurrent.Executors

class DownloadRepositoryImpl : DownloadRepository {
    private val executor = Executors.newSingleThreadExecutor()
    private val downloadTasks = HashMap<String, DownloadTask>()

    override fun addNewDownloadTask(url: String): DownloadTask {
        val downloadTask = DownloadTask(url, "/sdcard/Download")
        downloadTasks[url] = downloadTask
        executor.execute(downloadTask)
        return downloadTask
    }

    override fun cancelDownload(taskId: String) {
        val downloadTask = downloadTasks[taskId]
        downloadTask?.cancel()
    }
}