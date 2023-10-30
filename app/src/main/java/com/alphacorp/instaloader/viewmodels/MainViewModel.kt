package com.alphacorp.instaloader.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alphacorp.instaloader.repository.MainRepository
import com.alphacorp.instaloader.utils.DownloadResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _postsCount = MutableLiveData<Int?>()
    val postsCount: MutableLiveData<Int?> = _postsCount

    private val _downloadResult = MutableLiveData<DownloadResult>()
    val downloadResult: LiveData<DownloadResult> = _downloadResult

    private val _infoVisible = MutableLiveData<Boolean>()
    val infoVisible: LiveData<Boolean> = _infoVisible

    private val _statusVisible = MutableLiveData<Boolean>()
    val statusVisible: LiveData<Boolean> = _statusVisible

    fun checkInput(input: String) {
        _infoVisible.postValue(false)
        _statusVisible.postValue(true)
        _postsCount.value = null
        _downloadResult.value = DownloadResult.Loading(message = "Loading...")

        if (input.isEmpty()) {
            _infoVisible.postValue(true)
            _statusVisible.postValue(false)
            _downloadResult.value = DownloadResult.Error(message = "Username or Link cannot be empty")
            return
        }

        if (input.startsWith("https://www.instagram.com/p/") || input.startsWith("https://www.instagram.com/reel/")) {
            _downloadResult.value = DownloadResult.Loading(message = "Checking Link...")
            val shortCode = input.substringAfter("https://www.instagram.com/").substringBefore("/")
            downloadPostsFromLink(shortCode)
        } else {
            _downloadResult.value = DownloadResult.Loading(message = "Checking Profile...")
            checkProfileExist(input)
        }
    }

    private fun checkProfileExist(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result =  mainRepository.checkProfileExist(userName)
            if (!result) {
                _downloadResult.postValue(DownloadResult.Error(message = "Profile is private or doesn't exists"))
                _infoVisible.postValue(true)
                _statusVisible.postValue(false)
            } else {
                _downloadResult.postValue(DownloadResult.Loading(message = "Downloading..."))
                downloadPosts(userName)
            }
        }
    }

    private suspend fun countPosts(userName: String) {
        val result = mainRepository.countPosts(userName)
        _postsCount.postValue(result)
    }

    private fun downloadPosts(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            countPosts(userName)
        }

        _downloadResult.value?.let { downloadResult ->
            if (downloadResult !is DownloadResult.Loading) {
                return
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val isDownloaded = mainRepository.downloadPosts(userName)

            _infoVisible.postValue(true)
            _statusVisible.postValue(false)

            if (isDownloaded) {
                _downloadResult.postValue(DownloadResult.Success)
            } else {
                _downloadResult.postValue(DownloadResult.Error(message = "Error occurred"))
            }
        }
    }

    private fun downloadPostsFromLink(shortCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _downloadResult.value = DownloadResult.Loading(message = "Downloading...")
            val result = mainRepository.downloadPostFromLink(shortCode)
            _infoVisible.postValue(true)
            _statusVisible.postValue(false)
            if (result) {
                _downloadResult.postValue(DownloadResult.Success)
            } else {
                _downloadResult.postValue(DownloadResult.Error(message = "Error Occurred"))
            }
        }
    }
}