package com.alphacorp.instaloader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alphacorp.instaloader.databinding.MainScreenBinding
import com.alphacorp.instaloader.utils.DownloadResult
import com.alphacorp.instaloader.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainScreen: AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: MainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        binding = DataBindingUtil.setContentView(this, R.layout.main_screen)

        binding.btnDownload.setOnClickListener {
            hideSoftKeyboard(this)
            val input = binding.etInput.text.toString().trim()
            mainViewModel.checkInput(input)
        }

        binding.llFooter.setOnClickListener {
            redirectToWebpage()
        }

        bindObservers()
    }

    private fun bindObservers() {
        mainViewModel.apply {
            postsCount.observe(this@MainScreen) { postCount ->
                binding.tvInfo.text = getString(R.string.post_count, postCount.toString())
            }

            infoVisible.observe(this@MainScreen) {
                binding.tvInfo.visibility = if (it) View.VISIBLE else View.GONE
            }

            statusVisible.observe(this@MainScreen) {
                binding.llStatus.visibility = if (it) View.VISIBLE else View.GONE
            }

            downloadResult.observe(this@MainScreen) {
                when (it) {
                    is DownloadResult.Loading -> {
                        binding.tvStatus.text = it.message
                        binding.tvInfo.visibility = View.GONE
                        binding.llStatus.visibility = View.VISIBLE
                    }
                    is DownloadResult.Success -> {
                        binding.tvInfo.text = "Download Completed!"
                        binding.llStatus.visibility = View.GONE
                        binding.tvInfo.visibility = View.VISIBLE
                    }
                    is DownloadResult.Error -> {
                        binding.tvInfo.text = it.message
                        binding.llStatus.visibility = View.GONE
                        binding.tvInfo.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun redirectToWebpage() {
        val repoURL = getString(R.string.project_link)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repoURL))
        startActivity(intent)
    }

    private fun hideSoftKeyboard(activity: Activity?) {
        if (activity == null) return
        if (activity.currentFocus == null) return
        val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}