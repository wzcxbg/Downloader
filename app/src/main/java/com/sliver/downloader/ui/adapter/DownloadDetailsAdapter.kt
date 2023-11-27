package com.sliver.downloader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sliver.downloader.databinding.ItemDownloadTaskDetailBinding
import com.sliver.downloader.ui.MainViewModel

class DownloadDetailsAdapter : RecyclerView.Adapter<DownloadDetailsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemDownloadTaskDetailBinding) : RecyclerView.ViewHolder(binding.root)

    private val downloadTaskDetails = ArrayList<MainViewModel.TaskDetail>()

    fun setItems(items: List<MainViewModel.TaskDetail>) {
        this.downloadTaskDetails.clear()
        this.downloadTaskDetails.addAll(items)
        notifyDataSetChanged()
    }

    fun getItems(): List<MainViewModel.TaskDetail> {
        return downloadTaskDetails.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDownloadTaskDetailBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = downloadTaskDetails[holder.adapterPosition]
        val binding = holder.binding
        binding.title.text = detail.taskId
        binding.progress.progress = detail.progress
    }

    override fun getItemCount(): Int {
        return downloadTaskDetails.size
    }
}