package com.sliver.downloader.domain

import com.sliver.downloader.domain.download.DownloadTask

interface DownloadRepository {
    fun addNewDownloadTask(url: String, listener: DownloadTask.DownloadListener): DownloadTask

    fun cancelDownload(taskId: String)
}