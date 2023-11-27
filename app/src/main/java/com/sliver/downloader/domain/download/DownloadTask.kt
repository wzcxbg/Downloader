package com.sliver.downloader.domain.download

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class DownloadTask(
    private val targetUrl: String,
    private val saveDir: String,
) : Runnable {
    override fun run() {
        val fileName = targetUrl.substringAfter("/")
        val saveFile = File(saveDir, fileName)
        saveFile.parentFile?.mkdirs()
        val connection = URL(targetUrl).openConnection() as HttpURLConnection
        val input = connection.inputStream
        val output = saveFile.outputStream()
        output.use {
            input.use {
                val buffer = ByteArray(8194)
                var len = input.read(buffer)
                while (len != -1) {
                    output.write(buffer, 0, len)
                    len = input.read(buffer)
                }
            }
        }
        connection.disconnect()
    }

    /**
     * 取消下载
     */
    fun cancel() {

    }
}