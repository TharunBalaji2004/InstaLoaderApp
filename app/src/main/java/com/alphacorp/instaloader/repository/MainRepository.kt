package com.alphacorp.instaloader.repository

import com.alphacorp.instaloader.loader.InstaLoader
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val instaLoader: InstaLoader
) {

}