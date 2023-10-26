package com.alphacorp.instaloader.loader

interface InstaLoader {
    fun countPosts(userName: String): Int
    fun downloadPosts(userName: String)
    fun downloadPostFromLink(shortcode: String)
}