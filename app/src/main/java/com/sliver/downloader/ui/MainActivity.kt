package com.sliver.downloader.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sliver.downloader.R
import com.sliver.downloader.base.BaseActivity
import com.sliver.downloader.databinding.ActivityMainBinding
import com.sliver.downloader.ui.adapter.DownloadDetailsAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel by viewModels<MainViewModel> { MainViewModel.Factory() }
    private val adapter = DownloadDetailsAdapter()

    override fun initView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun initListener() {
        viewModel.taskDetails
            .onEach { adapter.setItems(it) }
            .launchIn(lifecycleScope)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_task -> addNewTask()
                R.id.delete_first_task -> deleteTask()
            }
            true
        }
        requestPermission()
    }

    private fun addNewTask() {
        viewModel.addNewTask(
            "https://download.jetbrains.com.cn/toolbox/jetbrains-toolbox-2.1.1.18388.exe"
        )
    }

    private fun deleteTask() {
        val detail = adapter.getItems().firstOrNull()
        if (detail != null) {
            viewModel.deleteTask(detail)
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
    }
}