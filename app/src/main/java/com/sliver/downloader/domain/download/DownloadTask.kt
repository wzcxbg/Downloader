package com.sliver.downloader.domain.download

import com.sliver.downloader.ui.MainViewModel
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class DownloadTask(
    private val taskUrl: String,
    private val saveDir: String,
) : Runnable {
    private var listener: DownloadListener? = null

    fun setDownloadListener(listener: DownloadListener) {
        this.listener = listener
    }

    fun getSaveUrl(): String {
        return taskUrl
    }

    fun getSaveFile(): File {
        val urlPath = URL(taskUrl).path
        val fileName = urlPath.substringAfter("/")
        return File(saveDir, fileName)
    }

    override fun run() {
        val saveFile = getSaveFile()
        saveFile.parentFile?.mkdirs()
        val connection = URL(taskUrl).openConnection() as HttpURLConnection
        val input = connection.inputStream
        val output = saveFile.outputStream()
        output.use {
            input.use {
                var cur = 0L
                listener?.onStateChanged(MainViewModel.TaskState.DOWNLOADING)
                listener?.onProgressChanged(cur, connection.contentLength.toLong())
                val buffer = ByteArray(8196)
                var len = input.read(buffer)
                while (len != -1) {
                    output.write(buffer, 0, len)
                    cur += len
                    listener?.onProgressChanged(cur, connection.contentLength.toLong())
                    len = input.read(buffer)
                }
                listener?.onStateChanged(MainViewModel.TaskState.COMPLETED)
            }
        }
        connection.disconnect()
    }

    /**
     * 取消下载
     */
    fun cancel() {

    }

    interface DownloadListener {
        fun onProgressChanged(progress: Long, total: Long)
        fun onStateChanged(state: MainViewModel.TaskState)
    }
}