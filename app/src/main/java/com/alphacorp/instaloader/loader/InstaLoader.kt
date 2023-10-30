package com.alphacorp.instaloader.loader

interface InstaLoader {
    suspend fun checkProfileExist(userName: String): Boolean
    suspend fun countPosts(userName: String): Int
    suspend fun downloadPosts(userName: String): Boolean
    suspend fun downloadPostFromLink(shortcode: String): Boolean
}