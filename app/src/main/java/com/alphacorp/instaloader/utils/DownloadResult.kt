package com.alphacorp.instaloader.utils

sealed class DownloadResult {
    data class Loading(val message: String) : DownloadResult()
    data object Success : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}