package com.sliver.downloader.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sliver.downloader.databinding.ItemTaskDetailBinding
import com.sliver.downloader.ui.MainViewModel

class DownloadDetailsAdapter : RecyclerView.Adapter<DownloadDetailsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTaskDetailBinding) : RecyclerView.ViewHolder(binding.root)

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
            ItemTaskDetailBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = downloadTaskDetails[holder.adapterPosition]
        val binding = holder.binding
        val progress = when (detail.progressTotal == 0L) {
            true -> 0
            false -> (detail.progress * 100 / detail.progressTotal).toInt()
        }
        binding.title.text = detail.fileName
        binding.progress.progress = progress
        binding.state.text = detail.state.toString()
    }

    override fun getItemCount(): Int {
        return downloadTaskDetails.size
    }
}