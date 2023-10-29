package com.alphacorp.instaloader.repository

import com.alphacorp.instaloader.loader.InstaLoader
import com.alphacorp.instaloader.loader.InstaLoaderImpl
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val instaLoader: InstaLoaderImpl
) {
    suspend fun checkProfileExist(userName: String): Boolean {
        return instaLoader.checkProfileExist(userName)
    }
    suspend fun countPosts(userName: String): Int {
        return instaLoader.countPosts(userName)
    }

    suspend fun downloadPosts(userName: String): Int {
        return instaLoader.downloadPosts(userName)
    }

    suspend fun downloadPostFromLink(shortcode: String): Boolean {
        return instaLoader.downloadPostFromLink(shortcode)
    }

}