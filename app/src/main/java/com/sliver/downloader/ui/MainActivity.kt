package com.sliver.downloader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sliver.downloader.R
import com.sliver.downloader.databinding.ActivityMainBinding
import com.sliver.downloader.ui.adapter.DownloadDetailsAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val adapter = DownloadDetailsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(
            this,
            MainViewModel.Factory()
        ).get(MainViewModel::class.java)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.taskDetails.collect { adapter.setItems(it) }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_task -> {
                    viewModel.addNewTask(
                        "https://download.jetbrains.com.cn/toolbox/jetbrains-toolbox-2.1.1.18388.exe"
                    )
                }

                R.id.delete_first_task -> {
                    val detail = adapter.getItems().firstOrNull()
                    if (detail != null) {
                        viewModel.deleteTask(detail)
                    }
                }
            }
            true
        }
    }
}