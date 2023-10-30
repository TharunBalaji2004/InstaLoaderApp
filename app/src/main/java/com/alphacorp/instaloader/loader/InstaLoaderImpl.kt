package com.alphacorp.instaloader.loader

import com.chaquo.python.PyObject
import javax.inject.Inject

class InstaLoaderImpl @Inject constructor(
    private val script: PyObject
): InstaLoader {

    override suspend fun checkProfileExist(userName: String): Boolean {
        try {
            return script.callAttr("check_profile_exist", userName).toBoolean()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun countPosts(userName: String): Int {
        var postCount: Int

        try {
            val postCountFunction = script.callAttr("post_count", userName)
            postCount = postCountFunction.toInt()
        } catch (e: Exception) {
            postCount = 0
        }

        return postCount
    }

    override suspend fun downloadPosts(userName: String): Boolean {
        try {
            script.callAttr("download", userName)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun downloadPostFromLink(shortcode: String): Boolean {
        try {
            script.callAttr("download_post_from_link", shortcode)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}